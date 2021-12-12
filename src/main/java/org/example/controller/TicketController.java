package org.example.controller;

import org.apache.pdfbox.io.IOUtils;
import org.example.exception.PdfGenerationException;
import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.util.DocumentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Controller for all operations on Tickets.
 * @author Andrii Krokhta
 */
@Controller
public class TicketController {

	private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
	public static final String TICKET_VIEW_NAME = "ticket";

	private final BookingFacade facade;

	@Autowired
	public TicketController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new ticket and adds to it model data.
	 *
	 * @param ticket New ticket data.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping(value = "/ticket", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createTicket(@ModelAttribute Ticket ticket, ModelMap model){
		var bookedTicket =
				facade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getCategory(), ticket.getPlace());
		model.addAttribute("bookedTicket", bookedTicket);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Gets a list of tickets by user and adds it to model data.
	 *
	 * @param userId User id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/ticketsByUser")
	public String getTicketsByUser(@RequestParam("userId") long userId,
								   @RequestParam("pageSize") int pageSize,
								   @RequestParam("pageNum") int pageNum,
								   ModelMap model) {
		var tickets = facade.getBookedTickets(new User(userId, null, null), pageSize, pageNum);
		model.addAttribute("ticketsByUser", tickets);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Gets a list of tickets by user.
	 *
	 * @param userId User id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @return byte[] of pdf with ticket data.
	 */
	@GetMapping(value = "/ticketsByUser", headers = "Accept=application/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody byte[] getTicketsByUserPdf(@RequestParam("userId") long userId,
													@RequestParam("pageSize") int pageSize,
													@RequestParam("pageNum") int pageNum) {
		var tickets = facade.getBookedTickets(new User(userId, null, null), pageSize, pageNum);
		var generatedFile = DocumentUtil.writeToPdf(tickets);
		try {
			var stream = new FileSystemResource(generatedFile).getInputStream();
			return IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("Failed to generate and load PDF file.");
			throw new PdfGenerationException("Failed to generate and load PDF file.");
		}
	}

	/**
	 * Gets a list of tickets by event and adds it to model data.
	 *
	 * @param eventId Event id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/ticketsByEvent")
	public String getTicketsByEvent(@RequestParam("eventId") long eventId,
									@RequestParam("pageSize") int pageSize,
									@RequestParam("pageNum") int pageNum,
								   ModelMap model) {
		var tickets = facade.getBookedTickets(new Event(eventId, null, null), pageSize, pageNum);
		model.addAttribute("ticketsByEvent", tickets);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Deletes a ticket by id. Adds a boolean to model data with information if deletion was successful or not.
	 *
	 * @param id Id of the ticket to be deleted.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/deleteTicket")
	public String deleteTicket(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.cancelTicket(id);
		model.addAttribute("ticketDeleted", deleteSuccessful);
		return TICKET_VIEW_NAME;
	}

}
