package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.pdfbox.io.IOUtils;
import org.example.converter.XmlMarshaller;
import org.example.dto.TicketDto;
import org.example.enums.Category;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.example.exception.UnmarshallingException;
import org.example.entity.Ticket;
import org.example.entity.TicketBuilder;
import org.example.repository.TicketRepository;
import org.example.service.AccountService;
import org.example.service.EventService;
import org.example.service.TicketService;
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
public class TicketServiceImpl implements TicketService {

	public static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

	private final TicketRepository ticketRepository;
	private final ModelMapper mapper;
	private final XmlMarshaller xmlMarshaller;
	private final DocumentUtil<TicketDto> documentUtil;
	private final EventService eventService;
	private final AccountService accountService;

	@Autowired
	public TicketServiceImpl(TicketRepository repository,
							 ModelMapper mapper,
							 XmlMarshaller xmlMarshaller,
							 DocumentUtil<TicketDto> documentUtil,
							 EventService eventService,
							 AccountService accountService) {
		this.ticketRepository = repository;
		this.mapper = mapper;
		this.xmlMarshaller = xmlMarshaller;
		this.documentUtil = documentUtil;
		this.eventService = eventService;
		this.accountService = accountService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TicketDto bookTicket(Long userId, Long eventId, Category category, int place) {
		var ticketPrice = eventService.getEventById(eventId).getTicketPrice();
		accountService.chargeForTicket(userId, ticketPrice);
		var ticket = ticketRepository.save(new TicketBuilder().setUserId(userId)
											.setEventId(eventId)
											.setCategory(category)
											.setPlace(place)
											.createTicket());
		logger.info("Booked a ticket for user (id: {}), event (id: {}).", userId, eventId);
		return mapper.map(ticket, TicketDto.class);
	}

	/**
	 * Batch book ticket from the input file by parsing the file and calling bookTicket for each of the tickets.
	 *
	 * @param file input file.
	 */
	@Override
	@Transactional
	public Iterable<TicketDto> batchBookTickets(MultipartFile file) {
		try {
			var ticketsToBook = xmlMarshaller.parse(file.getInputStream(),
																new TypeReference<List<Ticket>>() {});
			return ticketsToBook.stream()
					.map(t -> bookTicket(t.getUser().getId(), t.getEvent().getId(), t.getCategory(), t.getPlace()))
					.collect(Collectors.toList());
		} catch (IOException e) {
			logger.warn("Failed to parse xml file.");
			throw new UnmarshallingException("Failed to parse xml file.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<TicketDto> getBookedTicketsByUserId(Long userId, Pageable pageable) {
		return mapper.map(ticketRepository.findByUserId(userId, pageable),
							new TypeToken<Page<TicketDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBookedTicketsByUserIdAsPdf(Long userId) {
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
	 * {@inheritDoc}
	 */
	@Override
	public Page<TicketDto> getBookedTicketsByEventId(Long eventId, Pageable pageable) {
		return mapper.map(ticketRepository.findByEventId(eventId, pageable),
				new TypeToken<Page<TicketDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void cancelTicket(Long ticketId) {
		var ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new EntityNotFoundException("Ticket with id {} does not exist." + ticketId));
		logger.info("Cancelling ticket with id {}.", ticketId);
		ticketRepository.deleteById(ticketId);
		accountService.refillAccount(ticket.getUser().getId(), ticket.getEvent().getTicketPrice());
	}
}
