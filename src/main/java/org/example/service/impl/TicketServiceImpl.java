package org.example.service.impl;

import org.example.dao.TicketRepository;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

	private final TicketRepository repository;

	@Autowired
	public TicketServiceImpl(TicketRepository repository) {
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticket bookTicket(long userId, long eventId, Ticket.Category category, int place) {
		return repository.save(new Ticket(0, new User(userId, null, null),
												new Event(eventId, null, null, null),
												category, place));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum) {
		return repository.getBookedTicketsByUserId(userId, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum) {
		return repository.getBookedTicketsByEventId(eventId, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancelTicket(long ticketId) {
		return repository.delete(ticketId);
	}
}
