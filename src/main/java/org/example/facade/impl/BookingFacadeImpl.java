package org.example.facade.impl;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

	private final EventService eventService;

	private final TicketService ticketService;

	private final UserService userService;

	private final AccountService accountService;

	@Autowired
	public BookingFacadeImpl(EventService eventService,
							 TicketService ticketService,
							 UserService userService,
							 AccountService accountService) {
		this.eventService = eventService;
		this.ticketService = ticketService;
		this.userService = userService;
		this.accountService = accountService;
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
	public List<EventDto> getEventsByTitle(String title, Pageable pageable) {
		return eventService.getEventsByTitle(title, pageable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsByDate(LocalDate day, Pageable pageable) {
		return eventService.getEventsForDay(day, pageable);
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
	public EventDto updateEvent(Long id, EventDto event) {
		return eventService.updateEvent(id, event);
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
	public List<UserDto> getUsersByName(String name, Pageable pageable) {
		return userService.getUsersByName(name, pageable);
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
	public UserDto updateUser(Long id, UserDto user) {
		return userService.updateUser(id, user);
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
	public Iterable<TicketDto> batchBookTickets(MultipartFile file) {
		return ticketService.batchBookTickets(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByUserId(Long userId, Pageable pageable) {
		return ticketService.getBookedTicketsByUserId(userId, pageable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TicketDto> getBookedTicketsByEventId(Long eventId, Pageable pageable) {
		return ticketService.getBookedTicketsByEventId(eventId, pageable);
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
