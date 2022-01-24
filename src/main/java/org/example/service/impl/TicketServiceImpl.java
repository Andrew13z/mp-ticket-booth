package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
import org.example.dto.TicketDto;
import org.example.enums.Category;
import org.example.exception.UnmarshallingException;
import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.example.repository.TicketRepository;
import org.example.service.TicketService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

	@Autowired
	public TicketServiceImpl(TicketRepository repository, ModelMapper mapper, XmlMarshaller xmlMarshaller) {
		this.ticketRepository = repository;
		this.mapper = mapper;
		this.xmlMarshaller = xmlMarshaller;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TicketDto bookTicket(Long userId, Long eventId, Category category, int place) {
		var ticket = ticketRepository.save(new TicketBuilder().setUserId(userId)
				.setEventId(eventId)
				.setCategory(category)
				.setPlace(place)
				.createTicket());
		logger.info("Booked a ticket for user (id: {}), event (id: {}).", userId, eventId);
		return mapper.map(ticket, TicketDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<TicketDto> batchBookTickets(MultipartFile file) {
		try {
			return xmlMarshaller.parse(file.getInputStream(), new TypeReference<List<TicketDto>>() {});
		} catch (IOException e) {
			logger.warn("Failed to parse xml file.");
			throw new UnmarshallingException("Failed to parse xml file.", e);
		}
	}

	/**
	 * Book tickets from the collection.
	 *
	 * @param ticketDtos Collection of tickets.
	 * @return List of booked tickets.
	 */
	private Iterable<TicketDto> bookTickets(List<TicketDto> ticketDtos) {
		List<Ticket> tickets = mapper.map(ticketDtos, new TypeToken<List<Ticket>>() {}.getType());
		return mapper.map(ticketRepository.saveAll(tickets), new TypeToken<Iterable<TicketDto>>() {}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByUserId(Long userId, Pageable pageable) {
		return ticketRepository.findByUserId(userId, pageable)
				.stream()
				.map(user -> mapper.map(user, TicketDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByEventId(Long eventId, Pageable pageable) {
		return ticketRepository.findByEventId(eventId, pageable)
				.stream()
				.map(user -> mapper.map(user, TicketDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelTicket(Long ticketId) {
		logger.info("Cancelling ticket with id {}.", ticketId);
		ticketRepository.deleteById(ticketId);
	}
}
