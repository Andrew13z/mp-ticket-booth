package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.pdfbox.io.IOUtils;
import org.example.converter.XmlMarshaller;
import org.example.dto.TicketDto;
import org.example.enums.Category;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.example.exception.UnmarshallingException;
import org.example.entity.TicketBuilder;
import org.example.repository.TicketRepository;
import org.example.util.DocumentUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

	public static final Logger logger = LoggerFactory.getLogger(TicketService.class);

	private final TicketRepository ticketRepository;
	private final ModelMapper mapper;
	private final XmlMarshaller xmlMarshaller;
	private final DocumentUtil<TicketDto> documentUtil;
	private final EventService eventService;
	private final UserService userService;

	@Autowired
	public TicketService(TicketRepository repository,
						 ModelMapper mapper,
						 XmlMarshaller xmlMarshaller,
						 DocumentUtil<TicketDto> documentUtil,
						 EventService eventService,
						 UserService userService) {
		this.ticketRepository = repository;
		this.mapper = mapper;
		this.xmlMarshaller = xmlMarshaller;
		this.documentUtil = documentUtil;
		this.eventService = eventService;
		this.userService = userService;
	}

	/**
	 * Book ticket for a specified event on behalf of specified user and charge the ticket price to the user's account.
	 *
	 * @param userId   User Id.
	 * @param eventId  Event Id.
	 * @param place    Place number.
	 * @param category Service category.
	 * @return Booked ticket object.
	 */
	@Transactional
	public TicketDto bookTicket(String userId, String eventId, Category category, int place) {
		var ticketPrice = eventService.getEventById(eventId).getTicketPrice();
		userService.chargeForTicket(userId, ticketPrice);
		var ticket = ticketRepository.save(new TicketBuilder().setUserId(userId)
											.setEventId(eventId)
											.setCategory(category)
											.setPlace(place)
											.createTicket());
		logger.info("Booked a ticket for user (id: {}), event (id: {}).", userId, eventId);
		return mapper.map(ticket, TicketDto.class);
	}

	/**
	 * Batch book ticket from the input file.
	 *
	 * @param file input file.
	 */
	@Transactional
	public Iterable<TicketDto> batchBookTickets(MultipartFile file) {
		try {
			var ticketsToBook = xmlMarshaller.parse(file.getInputStream(),
																new TypeReference<List<TicketDto>>() {});
			return ticketsToBook.stream()
					.map(t -> bookTicket(t.getUser().getId(), t.getEvent().getId(), t.getCategory(), t.getPlace()))
					.collect(Collectors.toList());
		} catch (IOException e) {
			logger.warn("Failed to parse xml file.");
			throw new UnmarshallingException("Failed to parse xml file.", e);
		}
	}

	/**
	 * Get all booked tickets by specified user id. Tickets should be sorted by event date in descending order.
	 *
	 * @param userId     User id
	 * @param pageable Pageable
	 * @return List of Ticket objects.
	 */
	public Page<TicketDto> getBookedTicketsByUserId(String userId, Pageable pageable) {
		return mapper.map(ticketRepository.findByUserId(userId, pageable),
							new TypeToken<Page<TicketDto>>(){}.getType());
	}

	/**
	 * Gets a list of tickets by user id in pdf format.
	 *
	 * @param userId   User id.
	 * @return byte[] of pdf with ticket data.
	 */
	public byte[] getBookedTicketsByUserIdAsPdf(String userId) {
		var ticketsByUserId = getBookedTicketsByUserId(userId, Pageable.ofSize(100));
		var generatedFile = documentUtil.writeToPdf(ticketsByUserId.getContent());
		try {
			var stream = new FileSystemResource(generatedFile).getInputStream();
			return IOUtils.toByteArray(stream);
		} catch (IOException e) {
			throw new PdfGenerationException("Failed to get generate PDF file.", e);
		}
	}

	/**
	 * Get all booked tickets by specified event id. Tickets should be sorted in by user email in ascending order.
	 *
	 * @param eventId    Event id
	 * @param pageable Pageable
	 * @return List of Ticket objects.
	 */
	public Page<TicketDto> getBookedTicketsByEventId(String eventId, Pageable pageable) {
		return mapper.map(ticketRepository.findByEventId(eventId, pageable),
				new TypeToken<Page<TicketDto>>(){}.getType());
	}

	/**
	 * Cancel ticket with a specified id. And refunds the ticket price to the account.
	 *
	 * @param ticketId Ticket id.
	 */
	@Transactional
	public void cancelTicket(String ticketId) {
		var ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new EntityNotFoundException("Ticket with id {} does not exist." + ticketId));
		logger.info("Cancelling ticket with id {}.", ticketId);
		ticketRepository.deleteById(ticketId);
		var ticketPrice = eventService.getEventById(ticket.getEventId())
				.getTicketPrice();
		userService.refillBalance(ticket.getUserId(), ticketPrice);
	}
}
