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

	@Autowired
	private TicketRepository repository;

	@Override
	public Ticket bookTicket(long userId, long eventId, Ticket.Category category, int place) {
		return repository.save(new Ticket(0, userId, eventId, category, place));
	}

	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return repository.getBookedTickets(user, pageSize, pageNum);
	}

	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return repository.getBookedTickets(event, pageSize, pageNum);
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return repository.delete(ticketId);
	}
}
