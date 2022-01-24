package org.example.controller;

import org.example.dto.TicketDto;
import org.example.facade.BookingFacade;
import org.example.validation.group.OnTicketCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for all operations on Tickets.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

	private final BookingFacade facade;

	@Autowired
	public TicketController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new ticket and charges the ticket price to the user's account.
	 *
	 * @param ticket New ticket data.
	 * @return Created ticket.
	 */
	@PostMapping
	@Transactional
	@Validated(OnTicketCreate.class)
	public TicketDto createTicket(@RequestBody TicketDto ticket) {
		var ticketPrice = facade.getEventById(ticket.getEvent().getId()).getTicketPrice();
		facade.chargeAccountForTicket(ticket.getUser().getId(), ticketPrice);
		return facade.bookTicket(ticket.getUser().getId(),
				ticket.getEvent().getId(),
				ticket.getCategory(),
				ticket.getPlace());
	}

	/**
	 * Gets a list of tickets by user.
	 *
	 * @param userId   User id.
	 * @param pageable Pageable.
	 * @return List of tickets, or if none is found, empty list.
	 */
	@GetMapping("/byUser")
	public List<TicketDto> getTicketsByUser(@RequestParam("userId") long userId, Pageable pageable) {
		return facade.getBookedTicketsByUserId(userId, pageable);
	}

	/**
	 * Gets a list of tickets by user in pdf format.
	 *
	 * @param userId   User id.
	 * @return byte[] of pdf with ticket data.
	 */
	@GetMapping(value = "/byUser", headers = "Accept=application/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public byte[] getTicketsByUserAsPdf(@RequestParam("userId") Long userId) {
		return facade.getBookedTicketsByUserIdAsPdf(userId);
	}

	/**
	 * Uploads a file with ticket data and saves the data.
	 *
	 * @return List of saved tickets.
	 */
	@PostMapping(value = "/batch")
	@Transactional
	public Iterable<TicketDto> batchBookTicketsFromFile(@RequestParam("file") MultipartFile file) {
		return facade.batchBookTickets(file);
	}

	/**
	 * Gets a list of tickets by event.
	 *
	 * @param eventId  Event id.
	 * @param pageable Pageable.
	 * @return List of tickets, or if none is found, empty list.
	 */
	@GetMapping("/byEvent")
	public List<TicketDto> getTicketsByEvent(@RequestParam("eventId") Long eventId, Pageable pageable) {
		return facade.getBookedTicketsByEventId(eventId, pageable);
	}

	/**
	 * Deletes a ticket by id.
	 * @param id    Id of the ticket to be deleted.
	 */
	@PutMapping
	public void deleteTicket(@RequestBody Long id) {
		facade.cancelTicket(id);
	}
}
