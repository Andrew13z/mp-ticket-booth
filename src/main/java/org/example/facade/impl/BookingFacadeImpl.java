package org.example.facade.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

	private static final Logger logger = LoggerFactory.getLogger(BookingFacadeImpl.class);

	private final EventService eventService;

	private final TicketService ticketService;

	private final UserService userService;

	private final XmlMarshaller xmlMarshaller;

  @Value("${tickets.source}")
  private Resource ticketsFile;

  @Value("${users.source}")
  private Resource usersFile;

  @Value("${events.source}")
  private Resource eventsFile;


  @Autowired
	public BookingFacadeImpl(EventService eventService,
													 TicketService ticketService,
													 UserService userService,
													 XmlMarshaller xmlMarshaller) {
		this.eventService = eventService;
		this.ticketService = ticketService;
		this.userService = userService;
		this.xmlMarshaller = xmlMarshaller;
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
	 * @return List of parsed tickets
	 */
	private List<Ticket> parseTicketListFromXml() {
		try {
			return xmlMarshaller.parse(ticketsFile, new TypeReference<>() {});
		} catch (IOException e) {
      logger.warn("Failed to load ticket data: {}", e.getMessage(), e);
      return Collections.emptyList();
		}
	}

	/**
	 * Parses user list form xml file.
	 *
	 * @return List of parsed users
	 */
	private List<User> parseUserListFromXml() {
		try {
			return xmlMarshaller.parse(usersFile, new TypeReference<>() {});
		} catch (IOException e) {
      logger.warn("Failed to load user data: {}", e.getMessage(), e);
      return Collections.emptyList();
		}
	}

	/**
	 * Parses event list form xml file.
	 *
	 * @return List of parsed events
	 */
	private List<Event> parseEventListFromXml() {
		try {
      return xmlMarshaller.parse(eventsFile, new TypeReference<>() {});
		} catch (IOException e) {
			logger.warn("Failed to load event data: {}", e.getMessage(), e);
      return Collections.emptyList();
		}
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
