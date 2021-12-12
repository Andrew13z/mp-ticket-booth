package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class EventController {

	private static final String EVENT_VIEW_NAME = "event";
	private final BookingFacade facade;

	@Autowired
	public EventController(BookingFacade facade) {
		this.facade = facade;
	}

	@PostMapping(value = "/event", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createEvent(@ModelAttribute Event event, ModelMap model){
		var createdEvent = facade.createEvent(new Event(0L, event.getTitle(), event.getDate()));
		model.addAttribute("createdEvent", createdEvent);
		return EVENT_VIEW_NAME;
	}

	@GetMapping("/event")
	public String getEventById(@RequestParam("id") long id, ModelMap model) {
		var event = facade.getEventById(id);
		model.addAttribute("eventById", event);
		return "event";
	}

	@GetMapping("/eventsByTitle")
	public String getEventsByTitle(@RequestParam("title") String title,
								 @RequestParam("pageSize") int pageSize,
								 @RequestParam("pageNum") int pageNum,
								 ModelMap model) {
		var events = facade.getEventsByTitle(title, pageSize, pageNum);
		model.addAttribute("eventsByTitle", events);
		return "event";
	}

	@GetMapping("/eventsByDate")
	public String getEventsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
								 @RequestParam("pageSize") int pageSize,
								 @RequestParam("pageNum") int pageNum,
								 ModelMap model) {
		var events = facade.getEventsForDay(date, pageSize, pageNum);
		model.addAttribute("eventsByDate", events);
		return "event";
	}

	@PostMapping("/updateEvent")
	public String updateEvent (@ModelAttribute Event event, ModelMap model) {
		var updatedEvent = facade.updateEvent(event);
		model.addAttribute("updatedEvent", updatedEvent);
		return "event";
	}

	@PostMapping("/deleteEvent")
	public String deleteEvent(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.deleteEvent(id);
		model.addAttribute("eventDeleted", deleteSuccessful);
		return "event";
	}
}
