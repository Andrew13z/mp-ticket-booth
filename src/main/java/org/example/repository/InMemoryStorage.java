package org.example.repository;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.util.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
@DependsOn("dataInitializer")
public class InMemoryStorage {

	private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

	private Map<Long, Ticket> tickets;

	private  Map<Long, Event> events;

	private  Map<Long, User> users;

	private long userIndex = 1;

	private long eventIndex = 1;

	private long ticketIndex = 1;

	@PostConstruct
	private void postConstruct() {
		tickets = DataInitializer.initializeTickets();
		logger.info("Initialized {} tickets.", tickets.size());
		users = DataInitializer.initializeUsers();
		logger.info("Initialized {} users.", users.size());
		events = DataInitializer.initializeEvents();
		logger.info("Initialized {} events.", events.size());

		ticketIndex += tickets.size();
		userIndex += users.size();
		eventIndex += events.size();
	}

	public  Map<Long, Ticket> getTickets() {
		return tickets;
	}

	public  Map<Long, Event> getEvents() {
		return events;
	}

	public Map<Long, User> getUsers() {
		return users;
	}

	public long getUserIndex() {
		return userIndex++;
	}

	public long getEventIndex() {
		return eventIndex++;
	}

	public long getTicketIndex() {
		return ticketIndex++;
	}
}
