package org.example.service;

import org.example.dto.TicketDto;
import org.example.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface TicketService {

	/**
	 * Book ticket for a specified event on behalf of specified user and charge the ticket price to the user's account.
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
	Page<TicketDto> getBookedTicketsByUserId(Long userId, Pageable pageable);

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
	Page<TicketDto> getBookedTicketsByEventId(Long eventId, Pageable pageable);

	/**
	 * Cancel ticket with a specified id. And refunds the ticket price to the account.
	 *
	 * @param ticketId Ticket id.
	 */
	void cancelTicket(Long ticketId);
}
