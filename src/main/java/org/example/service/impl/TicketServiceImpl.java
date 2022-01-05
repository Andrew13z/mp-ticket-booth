package org.example.service.impl;

import org.example.dto.TicketDto;
import org.example.enums.Category;
import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.example.repository.TicketRepository;
import org.example.service.TicketService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;

	private final ModelMapper mapper;

	@Autowired
	public TicketServiceImpl(TicketRepository repository, ModelMapper mapper) {
		this.ticketRepository = repository;
		this.mapper = mapper;
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
		return mapper.map(ticket, TicketDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<TicketDto> bookTickets(List<TicketDto> ticketDtos) {
		List<Ticket> tickets = mapper.map(ticketDtos, new TypeToken<List<Ticket>>(){}.getType());
		return mapper.map(ticketRepository.saveAll(tickets),
				new TypeToken<Iterable<TicketDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum) {
		return ticketRepository.findByUserId(userId, PageRequest.of(pageNum, pageSize))
				.stream()
				.map(user -> mapper.map(user, TicketDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum) {
		return ticketRepository.findByEventId(eventId, PageRequest.of(pageNum, pageSize))
				.stream()
				.map(user -> mapper.map(user, TicketDto.class))
				.collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelTicket(Long ticketId) {
		ticketRepository.deleteById(ticketId);
	}
}
