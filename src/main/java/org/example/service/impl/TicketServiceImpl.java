package org.example.service.impl;

import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.example.repository.TicketRepository;
import org.example.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

	private final TicketRepository ticketRepository;

	@Autowired
	public TicketServiceImpl(TicketRepository repository) {
		this.ticketRepository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticket bookTicket(Long userId, Long eventId, Ticket.Category category, int place) {
		return ticketRepository.save(new TicketBuilder().setUserId(userId)
													.setEventId(eventId)
													.setCategory(category)
													.setPlace(place)
													.createTicket());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Ticket> bookTickets(List<Ticket> tickets) {
		return ticketRepository.saveAll(tickets);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum) {
		return ticketRepository.findByUserId(userId, PageRequest.of(pageNum, pageSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum) {
		return ticketRepository.findByEventId(eventId, PageRequest.of(pageNum, pageSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelTicket(Long ticketId) {
		ticketRepository.deleteById(ticketId);
	}
}
