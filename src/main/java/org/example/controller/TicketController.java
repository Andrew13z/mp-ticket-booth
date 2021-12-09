package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TicketController {

	private final BookingFacade facade;

	@Autowired
	public TicketController(BookingFacade facade) {
		this.facade = facade;
	}

	@PostMapping(value = "/ticket", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createTicket(@ModelAttribute Ticket ticket, ModelMap model){
		var bookedTicket =
				facade.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getCategory(), ticket.getPlace());
		model.addAttribute("bookedTicket", bookedTicket);
		return "ticket";
	}

	@GetMapping("/ticketsByUser")
	public String getTicketsByUser(@RequestParam("userId") long userId,
								   @RequestParam("pageSize") int pageSize,
								   @RequestParam("pageNum") int pageNum,
								   ModelMap model) {
		var tickets = facade.getBookedTickets(new User(userId, null, null), pageSize, pageNum);
		model.addAttribute("ticketsByUser", tickets);
		return "ticket";
	}

	@GetMapping("/ticketsByEvent")
	public String getTicketsByEvent(@RequestParam("eventId") long eventId,
								   @RequestParam("pageSize") int pageSize,
								   @RequestParam("pageNum") int pageNum,
								   ModelMap model) {
		var tickets = facade.getBookedTickets(new Event(eventId, null, null), pageSize, pageNum);
		model.addAttribute("ticketsByEvent", tickets);
		return "ticket";
	}

	@PostMapping("/deleteTicket")
	public String deleteTicket(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.cancelTicket(id);
		model.addAttribute("ticketDeleted", deleteSuccessful);
		return "ticket";
	}

}
