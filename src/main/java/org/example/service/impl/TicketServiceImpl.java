package org.example.service.impl;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.TicketService;

import java.util.List;

public class TicketServiceImpl implements TicketService {

	@Override
	public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
		return null;
	}

	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return null;
	}

	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return null;
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return false;
	}
}
