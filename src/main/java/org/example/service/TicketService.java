package org.example.service;

import org.example.model.Ticket;

import java.util.List;

public interface TicketService {

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
	Ticket bookTicket(long userId, long eventId, Ticket.Category category, int place);

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
	boolean cancelTicket(long ticketId);
}
