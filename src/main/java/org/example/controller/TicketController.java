package org.example.controller;

import org.example.dto.TicketDto;
import org.example.facade.BookingFacade;
import org.example.validation.group.OnTicketCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for all operations on Tickets.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping(value = "/tickets")
@Validated
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
	@ResponseStatus(HttpStatus.CREATED)
	@Validated(OnTicketCreate.class)
	public TicketDto createTicket(@RequestBody @Valid TicketDto ticket) {
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
	@GetMapping
	public List<TicketDto> getTicketsByUser(@RequestParam(value = "userId", required = false) Long userId,
											@RequestParam(value = "eventId", required = false) Long eventId,
											Pageable pageable) {
		if (userId != null) {
			return facade.getBookedTicketsByUserId(userId, pageable);
		}
		if (eventId != null) {
			return facade.getBookedTicketsByEventId(eventId, pageable);
		}
		throw new IllegalArgumentException("No parameters provided to search by.");
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
	@ResponseStatus(HttpStatus.CREATED)
	public Iterable<TicketDto> batchBookTicketsFromFile(@RequestParam("file") MultipartFile file) {
		return facade.batchBookTickets(file);
	}

	/**
	 * Deletes a ticket by id.
	 * @param id    Id of the ticket to be deleted.
	 */
	@DeleteMapping
	@Transactional
	public void deleteTicket(@RequestBody Long id) {
		facade.cancelTicket(id);
	}
}
