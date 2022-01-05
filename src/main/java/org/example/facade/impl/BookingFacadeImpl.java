package org.example.facade.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
import org.example.dto.AccountDto;
import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.example.facade.BookingFacade;
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
	public EventDto getEventById(Long eventId) {
		return eventService.getEventById(eventId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsByTitle(String title, int pageSize, int pageNum) {
		return eventService.getEventsByTitle(title, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return eventService.getEventsForDay(day, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EventDto createEvent(EventDto event) {
		return eventService.createEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EventDto updateEvent(EventDto event) {
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
	public UserDto getUserById(Long userId) {
		return userService.getUserById(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto getUserByEmail(String email) {
		return userService.getUserByEmail(email);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDto> getUsersByName(String name, int pageSize, int pageNum) {
		return userService.getUsersByName(name, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto createUser(UserDto user) {
		return userService.createUser(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto updateUser(UserDto user) {
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
	public TicketDto bookTicket(Long userId, Long eventId, Category category, int place) {
		return ticketService.bookTicket(userId, eventId, category, place);
	}

	@Override
	public Iterable<TicketDto> batchBookTickets(InputStream stream) throws IOException {
		var tickets = xmlMarshaller.parse(stream, new TypeReference<List<TicketDto>>() {});
		return ticketService.bookTickets(tickets);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum) {
		return ticketService.getBookedTicketsByUserId(userId, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum) {
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
	public AccountDto createAccount(Long userId) {
		return accountService.createAccount(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDto refillAccount(Long accountId, BigDecimal refillSum) {
		return accountService.refillAccount(accountId, refillSum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDto chargeAccountForTicket(Long accountId, BigDecimal ticketPrice) {
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
