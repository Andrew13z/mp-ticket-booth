package org.example.facade;

import org.example.model.Account;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Groups together all operations related to ticket booking.
 * Created by maksym_govorischev.
 */
public interface BookingFacade {

	/**
	 * Gets event by its id.
	 *
	 * @return Event.
	 */
	Event getEventById(Long eventId);

	/**
	 * Get list of events by matching title. Title is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param title    Event title or it's part.
	 * @param pageSize Pagination param. Number of events to return on a page.
	 * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
	 * @return List of events.
	 */
	List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

	/**
	 * Get list of events for specified day.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param day      Date object from which day information is extracted.
	 * @param pageSize Pagination param. Number of events to return on a page.
	 * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
	 * @return List of events.
	 */
	List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum);

	/**
	 * Creates new event. Event id should be auto-generated.
	 *
	 * @param event Event data.
	 * @return Created Event object.
	 */
	Event createEvent(Event event);

	/**
	 * Updates event using given data.
	 *
	 * @param event Event data for update. Should have id set.
	 * @return Updated Event object.
	 */
	Event updateEvent(Event event);

	/**
	 * Deletes event by its id.
	 *
	 * @param eventId Event id.
	 */
	void deleteEvent(Long eventId);

	/**
	 * Gets user by its id.
	 *
	 * @return User.
	 */
	User getUserById(Long userId);

	/**
	 * Gets user by its email. Email is strictly matched.
	 *
	 * @return User.
	 */
	User getUserByEmail(String email);

	/**
	 * Get list of users by matching name. Name is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param name     Users name or it's part.
	 * @param pageSize Pagination param. Number of users to return on a page.
	 * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
	 * @return List of users.
	 */
	List<User> getUsersByName(String name, int pageSize, int pageNum);

	/**
	 * Creates new user. User id should be auto-generated.
	 *
	 * @param user User data.
	 * @return Created User object.
	 */
	User createUser(User user);

	/**
	 * Updates user using given data.
	 *
	 * @param user User data for update. Should have id set.
	 * @return Updated User object.
	 */
	User updateUser(User user);

	/**
	 * Deletes user by its id.
	 *
	 * @param userId User id.
	 */
	void deleteUser(Long userId);

	/**
	 * Book ticket for a specified event on behalf of specified user.
	 *
	 * @param userId   User Id.
	 * @param eventId  Event Id.
	 * @param place    Place number.
	 * @param category Service category.
	 * @return Booked ticket object.
	 * @throws java.lang.IllegalStateException if this place has already been booked.
	 */
	Ticket bookTicket(Long userId, Long eventId, Ticket.Category category, int place);

	/**
	 * Batch book ticket from the input stream.
	 *
	 * @param stream input stream.
	 */
	Iterable<Ticket> batchBookTickets(InputStream stream) throws IOException;

	/**
	 * Get all booked tickets by specified user id. Tickets should be sorted by event date in descending order.
	 *
	 * @param userId     User id
	 * @param pageSize Pagination param. Number of tickets to return on a page.
	 * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
	 * @return List of Ticket objects.
	 */
	List<Ticket> getBookedTicketsByUserId(Long userId, int pageSize, int pageNum);

	/**
	 * Get all booked tickets by specified event id. Tickets should be sorted in by user email in ascending order.
	 *
	 * @param eventId    Event id
	 * @param pageSize Pagination param. Number of tickets to return on a page.
	 * @param pageNum  Pagination param. Number of the page to return. Starts from 1.
	 * @return List of Ticket objects.
	 */
	List<Ticket> getBookedTicketsByEventId(Long eventId, int pageSize, int pageNum);

	/**
	 * Cancel ticket with a specified id.
	 *
	 * @param ticketId Ticket id.
	 * @return Flag whether anything has been canceled.
	 */
	void cancelTicket(Long ticketId);

	/**
	 * Creates and account for user id.
	 *
	 * @param userId User id.
	 * @return created account.
	 */
	Account createAccount(Long userId);

	/**
	 * Adds the provided refill sum to the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param refillSum Amount to be added to the account.
	 * @return updated account.
	 */
	Account refillAccount(Long accountId, BigDecimal refillSum);

	/**
	 * Subtracts the provided ticket price from the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param ticketPrice Amount to be subtracted from the account.
	 * @return updated account.
	 */
	Account chargeAccountForTicket(Long accountId, BigDecimal ticketPrice);

	/**
	 * Deletes account by its id.
	 *
	 * @param accountId Account id.
	 */
	void deleteAccount(Long accountId);

}
