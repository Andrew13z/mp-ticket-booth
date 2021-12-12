package org.example.repository;

import org.example.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TicketInMemoryStorage extends InMemoryStorage<Ticket> {

	private final Map<Long, Ticket> tickets = new HashMap<>();

	public TicketInMemoryStorage() {
		super(null);//todo
	}

	@Override
	public Map<Long, Ticket> getData() {
		return tickets;
	}
}
