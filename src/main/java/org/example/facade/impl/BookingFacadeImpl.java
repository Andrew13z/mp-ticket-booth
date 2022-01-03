package org.example.facade.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
import org.example.facade.BookingFacade;
import org.example.model.Account;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.EventService;
import org.example.service.TicketService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

	private final EventService eventService;

	private final TicketService ticketService;

	private final UserService userService;

	private final AccountService accountService;

	private final XmlMarshaller xmlMarshaller;

	@Autowired
	public BookingFacadeImpl(EventService eventService,
							 TicketService ticketService,
							 UserService userService,
							 AccountService accountService, XmlMarshaller xmlMarshaller) {
		this.eventService = eventService;
		this.ticketService = ticketService;
		this.userService = userService;
		this.accountService = accountService;
		this.xmlMarshaller = xmlMarshaller;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getEventById(Long eventId) {
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
	public void deleteEvent(Long eventId) {
		eventService.deleteEvent(eventId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserById(Long userId) {
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
	public void deleteUser(Long userId) {
		userService.deleteUser(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticket bookTicket(Long userId, Long eventId, Ticket.Category category, int place) {
		return ticketService.bookTicket(userId, eventId, category, place);
	}

	@Override
	public List<Ticket> batchBookTickets(InputStream stream) throws IOException {
		var tickets = xmlMarshaller.parse(stream, new TypeReference<List<Ticket>>() {
		});
		tickets.forEach(ticket -> bookTicket(ticket.getUser().getId(), ticket.getEvent().getId(), ticket.getCategory(), ticket.getPlace()));
		return tickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum) {
		return ticketService.getBookedTicketsByUserId(userId, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum) {
		return ticketService.getBookedTicketsByEventId(eventId, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void cancelTicket(Long ticketId) {
		ticketService.cancelTicket(ticketId);
	}


	@Override
	public Account createAccount(Long userId) {
		return accountService.createAccount(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account refillAccount(Long accountId, BigDecimal refillSum) {
		return accountService.refillAccount(accountId, refillSum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account chargeAccountForTicket(Long accountId, BigDecimal ticketPrice) {
		return accountService.chargeForTicket(accountId, ticketPrice);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAccount(Long accountId) {
		accountService.deleteById(accountId);
	}
}
