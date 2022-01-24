package org.example.facade;

import org.example.dto.AccountDto;
import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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
	EventDto getEventById(Long eventId);

	/**
	 * Get list of events by matching title. Title is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param title    Event title or it's part.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	List<EventDto> getEventsByTitle(String title, Pageable pageable);

	/**
	 * Get list of events for specified day.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param day      Date object from which day information is extracted.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	List<EventDto> getEventsByDate(LocalDate day, Pageable pageable);

	/**
	 * Creates new event. Event id should be auto-generated.
	 *
	 * @param event Event data.
	 * @return Created Event object.
	 */
	EventDto createEvent(EventDto event);

	/**
	 * Updates event using given data.
	 *
	 * @param id Id of the event to be  updated.
	 * @param event Event data for update. Should have id set.
	 * @return Updated Event object.
	 */
	EventDto updateEvent(Long id, EventDto event);

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
	UserDto getUserById(Long userId);

	/**
	 * Gets user by its email. Email is strictly matched.
	 *
	 * @return User.
	 */
	UserDto getUserByEmail(String email);

	/**
	 * Get list of users by matching name. Name is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param name     Users name or it's part.
	 * @param pageable Pageable
	 * @return List of users.
	 */
	List<UserDto> getUsersByName(String name, Pageable pageable);

	/**
	 * Creates new user. User id should be auto-generated.
	 *
	 * @param user User data.
	 * @return Created User object.
	 */
	UserDto createUser(UserDto user);

	/**
	 * Updates user using given data.
	 *
	 * @param id Id of the user to be  updated.
	 * @param user User data for update. Should have id set.
	 * @return Updated User object.
	 */
	UserDto updateUser(Long id, UserDto user);

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
	TicketDto bookTicket(Long userId, Long eventId, Category category, int place);

	/**
	 * Batch book ticket from the input file.
	 *
	 * @param file input file.
	 */
	Iterable<TicketDto> batchBookTickets(MultipartFile file);

	/**
	 * Get all booked tickets by specified user id. Tickets should be sorted by event date in descending order.
	 *
	 * @param userId     User id
	 * @param pageable Pageable
	 * @return List of Ticket objects.
	 */
	List<TicketDto> getBookedTicketsByUserId(Long userId, Pageable pageable);

	/**
	 * Gets a list of tickets by user id in pdf format.
	 *
	 * @param userId   User id.
	 * @return byte[] of pdf with ticket data.
	 */
	byte[] getBookedTicketsByUserIdAsPdf(Long userId);

	/**
	 * Get all booked tickets by specified event id. Tickets should be sorted in by user email in ascending order.
	 *
	 * @param eventId    Event id
	 * @param pageable Pageable
	 * @return List of Ticket objects.
	 */
	List<TicketDto> getBookedTicketsByEventId(Long eventId, Pageable pageable);

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
	AccountDto createAccount(Long userId);

	/**
	 * Adds the provided refill sum to the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param refillSum Amount to be added to the account.
	 * @return updated account.
	 */
	AccountDto refillAccount(Long accountId, BigDecimal refillSum);

	/**
	 * Subtracts the provided ticket price from the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param ticketPrice Amount to be subtracted from the account.
	 * @return updated account.
	 */
	AccountDto chargeAccountForTicket(Long accountId, BigDecimal ticketPrice);

	/**
	 * Deletes account by its id.
	 *
	 * @param accountId Account id.
	 */
	void deleteAccount(Long accountId);

}
