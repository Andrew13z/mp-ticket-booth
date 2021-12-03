package org.example.dao;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TicketRepository extends InMemoryRepository<Long, Ticket>{

	private static final Logger logger = LoggerFactory.getLogger(TicketRepository.class);

	private InMemoryStorage<Ticket> storage;

	@Autowired
	public void setStorage(InMemoryStorage<Ticket> storage) {
		this.storage = storage;
	}

	@Override
	public Map<Long, Ticket> getData() {
		return storage.getData();
	}

	@Override
	public Ticket save(Ticket ticket) {
		var index = storage.getIndex();
		ticket.setId(index);
		getData().put(index, ticket);
		logger.info("Saved ticket with id {}.", index);
		return ticket;
	}

	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(ticket -> ticket.getUserId() == user.getId())
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(ticket -> ticket.getEventId() == event.getId())
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
