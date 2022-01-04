package org.example.controller;

import org.apache.pdfbox.io.IOUtils;
import org.example.exception.PdfGenerationException;
import org.example.facade.BookingFacade;
import org.example.model.Ticket;
import org.example.util.DocumentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for all operations on Tickets.
 *
 * @author Andrii Krokhta
 */
@Controller
@RequestMapping("/ticket")
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
	 * @param model  Model data.
	 * @return Name of the view.
	 */
	@PostMapping
	@Transactional
	public String createTicket(@ModelAttribute Ticket ticket, ModelMap model) {
		var ticketPrice = facade.getEventById(ticket.getEvent().getId()).getTicketPrice();
		facade.chargeAccountForTicket(ticket.getUser().getId(), ticketPrice);
		var createdTicket = facade.bookTicket(ticket.getUser().getId(),
													ticket.getEvent().getId(),
													ticket.getCategory(),
													ticket.getPlace());
		model.addAttribute("createdTicket", createdTicket);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Gets a list of tickets by user and adds it to model data.
	 *
	 * @param userId   User id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum  Number of page to display.
	 * @param model    Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byUser")
	public String getTicketsByUser(@RequestParam("userId") long userId,
								   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
								   @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
								   ModelMap model) {
		var tickets = facade.getBookedTicketsByUserId(userId, pageSize, pageNum);
		model.addAttribute("ticketsByUser", tickets);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Gets a list of tickets by user.
	 *
	 * @param userId   User id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum  Number of page to display.
	 * @return byte[] of pdf with ticket data.
	 */
	@GetMapping(value = "/byUser", headers = "Accept=application/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public @ResponseBody
	byte[] getTicketsByUserPdf(
			@RequestParam("userId") Long userId,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum) {
		var tickets = facade.getBookedTicketsByUserId(userId, pageSize, pageNum);
		var generatedFile = DocumentUtil.writeToPdf(tickets);
		try {
			var stream = new FileSystemResource(generatedFile).getInputStream();
			return IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("Failed to generate and load PDF file: {}", e.getMessage());
			throw new PdfGenerationException("Failed to generate and load PDF file.");
		}
	}

	/**
	 * Uploads a file with ticket data and save the data.
	 *
	 * @return byte[] of pdf with ticket data.
	 */
	@PostMapping(value = "/batch")
	@Transactional
	public String batchBookTicketsFromFile(@RequestParam("file") MultipartFile file, ModelMap model) {
		Iterable<Ticket> savedTickets = null;
		try {
			savedTickets = facade.batchBookTickets(file.getInputStream());
		} catch (IOException e) {
			logger.warn("Failed to load tickets from a file. {}", e.getMessage());
		}
		if (savedTickets != null) {
			model.addAttribute("batchBookedTickets", savedTickets);
			return TICKET_VIEW_NAME;
		} else {
			return "error";
		}
	}

	/**
	 * Gets a list of tickets by event and adds it to model data.
	 *
	 * @param eventId  Event id.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum  Number of page to display.
	 * @param model    Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byEvent")
	public String getTicketsByEvent(@RequestParam("eventId") Long eventId,
									@RequestParam("pageSize") int pageSize,
									@RequestParam("pageNum") int pageNum,
									ModelMap model) {
		var tickets = facade.getBookedTicketsByEventId(eventId, pageSize, pageNum);
		model.addAttribute("ticketsByEvent", tickets);
		return TICKET_VIEW_NAME;
	}

	/**
	 * Deletes a ticket by id. Adds a boolean to model data with information if deletion was successful or not.
	 *
	 * @param id    Id of the ticket to be deleted.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/delete")
	public String deleteTicket(@RequestParam("id") Long id, ModelMap model) {
		facade.cancelTicket(id);
		model.addAttribute("deleteTicketId", id);
		return TICKET_VIEW_NAME;
	}
}
