package org.example.facade.impl;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.TicketService;
import org.example.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

	private final EventService eventService;

	private final TicketService ticketService;

	private final UserService userService;

	public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService) {
		this.eventService = eventService;
		this.ticketService = ticketService;
		this.userService = userService;
	}

	@Override
	public Event getEventById(long eventId) {
		return eventService.getEventById(eventId);
	}

	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventService.getEventsByTitle(title, pageSize, pageNum);
	}

	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return eventService.getEventsForDay(day, pageSize, pageNum);
	}

	@Override
	public Event createEvent(Event event) {
		return eventService.createEvent(event);
	}

	@Override
	public Event updateEvent(Event event) {
		return eventService.updateEvent(event);
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return eventService.deleteEvent(eventId);
	}

	@Override
	public User getUserById(long userId) {
		return userService.getUserById(userId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userService.getUserByEmail(email);
	}

	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return userService.getUsersByName(name, pageSize, pageNum);
	}

	@Override
	public User createUser(User user) {
		return userService.createUser(user);
	}

	@Override
	public User updateUser(User user) {
		return userService.updateUser(user);
	}

	@Override
	public boolean deleteUser(long userId) {
		return userService.deleteUser(userId);
	}

	@Override
	public Ticket bookTicket(long userId, long eventId, Ticket.Category category, int place) {
		return ticketService.bookTicket(userId, eventId, category, place);
	}

	@Override
	public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(user, pageSize, pageNum);
	}

	@Override
	public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
		return ticketService.getBookedTickets(event, pageSize, pageNum);
	}

	@Override
	public boolean cancelTicket(long ticketId) {
		return ticketService.cancelTicket(ticketId);
	}
}
