package org.example.facade.impl;

import org.example.converter.XmlConverter;
import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.TicketService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

	private static final Logger logger = LoggerFactory.getLogger(BookingFacadeImpl.class);

	private final EventService eventService;

	private final TicketService ticketService;

	private final UserService userService;

	private final XmlConverter ticketXmlConverter;

	@Autowired
	public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService, XmlConverter ticketXmlConverter) {
		this.eventService = eventService;
		this.ticketService = ticketService;
		this.userService = userService;
		this.ticketXmlConverter = ticketXmlConverter;
	}

	/**
	 * Preloads ticket data from xml file.
	 */
	@PostConstruct
	private void preloadData() {
		var tickets = parseTicketListFromXml();
		tickets.forEach(ticket -> ticketService.bookTicket(ticket.getUserId(), ticket.getEventId(),
				ticket.getCategory(), ticket.getPlace()));
		logger.info("Loaded ticket data with {} entries.", tickets.size());
		var users = parseUserListFromXml();
		users.forEach(user -> userService.createUser(new User(user.getId(), user.getName(), user.getEmail())));
		logger.info("Loaded user data with {} entries.", users.size());
		var events = parseEventListFromXml();
		events.forEach(event -> eventService.createEvent(new Event(event.getId(), event.getTitle(), event.getDate())));
		logger.info("Loaded event data with {} entries.", events.size());
	}

	/**
	 * Parses ticket list form xml file.
	 *
	 * @return List of pased tickets
	 */
	private List<Ticket> parseTicketListFromXml() {
		List<Ticket> tickets = new ArrayList<>();
		try {
			tickets = ticketXmlConverter.parseTicketXmlToObject();
		} catch (IOException e) {
			logger.warn("Failed to load ticket data.");
			e.printStackTrace();
		}
		return tickets;
	}

	/**
	 * Parses ticket list form xml file.
	 *
	 * @return List of pased tickets
	 */
	private List<User> parseUserListFromXml() {
		List<User> users = new ArrayList<>();
		try {
			users = ticketXmlConverter.parseUserXmlToObject();
		} catch (IOException e) {
			logger.warn("Failed to load user data.");
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * Parses ticket list form xml file.
	 *
	 * @return List of pased tickets
	 */
	private List<Event> parseEventListFromXml() {
		List<Event> events = new ArrayList<>();
		try {
			events = ticketXmlConverter.parseEventXmlToObject();
		} catch (IOException e) {
			logger.warn("Failed to load event data.");
			e.printStackTrace();
		}
		return events;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getEventById(long eventId) {
		return eventService.getEventById(eventId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventService.getEventsByTitle(title, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return eventService.getEventsForDay(day, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event createEvent(Event event) {
		return eventService.createEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event updateEvent(Event event) {
		return eventService.updateEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEvent(long eventId) {
		return eventService.deleteEvent(eventId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserById(long userId) {
		return userService.getUserById(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserByEmail(String email) {
		return userService.getUserByEmail(email);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return userService.getUsersByName(name, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createUser(User user) {
		return userService.createUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User updateUser(User user) {
		return userService.updateUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUser(long userId) {
		return userService.deleteUser(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticket bookTicket(long userId, long eventId, Ticket.Category category, int place) {
		return ticketService.bookTicket(userId, eventId, category, place);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(user, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(event, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancelTicket(long ticketId) {
		return ticketService.cancelTicket(ticketId);
	}
}
