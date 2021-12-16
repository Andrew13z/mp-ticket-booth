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

/**
 * Repository for all operations on Tickets.
 * @author Andrii Krokhta
 */
@Repository
public class TicketRepository extends InMemoryRepository<Long, Ticket>{

	private static final Logger logger = LoggerFactory.getLogger(TicketRepository.class);

	private final InMemoryStorage<Ticket> storage;

	@Autowired
	public TicketRepository(InMemoryStorage<Ticket> storage) {
		this.storage = storage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, Ticket> getData() {
		return storage.getData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticket save(Ticket ticket) {
		var index = storage.getIndex();
		ticket.setId(index);
		getData().put(index, ticket);
		logger.info("Saved ticket with id {}.", index);
		return ticket;
	}

	/**
	 * Gets a list of tickets by user.
	 *
	 * @param user User.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @return List of tickets or empty list if no tickets for the provided user are found.
	 */
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(ticket -> ticket.getUserId() == user.getId())
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	/**
	 * Gets a list of tickets by event.
	 *
	 * @param event Event.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @return List of tickets or empty list if no tickets for the provided event are found.
	 */
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(ticket -> ticket.getEventId() == event.getId())
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
