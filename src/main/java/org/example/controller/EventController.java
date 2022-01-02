package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * Controller for all operations on Events.
 * @author Andrii Krokhta
 */
@Controller
@RequestMapping("/event")
public class EventController {

	private static final String EVENT_VIEW_NAME = "event";
	private final BookingFacade facade;

	@Autowired
	public EventController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new event and adds to it model data.
	 *
	 * @param event New event data.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping
	public String createEvent(@ModelAttribute Event event, ModelMap model){
		var createdEvent = facade.createEvent(event);
		model.addAttribute("createdEvent", createdEvent);
		return EVENT_VIEW_NAME;
	}

	/**
	 * Gets an event by id and adds it to model data.
	 *
	 * @param id Event id.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping
	public String getEventById(@RequestParam("id") long id, ModelMap model) {
		var event = facade.getEventById(id);
		model.addAttribute("eventById", event);
		return EVENT_VIEW_NAME;
	}

	/**
	 * Gets a list of events by title and adds it to model data.
	 *
	 * @param title Event title.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byTitle")
	public String getEventsByTitle(@RequestParam("title") String title,
								   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
								   @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
								   ModelMap model) {
		var events = facade.getEventsByTitle(title, pageSize, pageNum);
		model.addAttribute("eventsByTitle", events);
		return EVENT_VIEW_NAME;
	}

	/**
	 * Gets a list of events by date and adds it to model data.
	 *
	 * @param date Event date.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byDate")
	public String getEventsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
								  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
								  @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
								  ModelMap model) {
		var events = facade.getEventsForDay(date, pageSize, pageNum);
		model.addAttribute("eventsByDate", events);
		return EVENT_VIEW_NAME;
	}

	/**
	 * Updates an event by event id and adds the updated object to model data.
	 *
	 * @param event Updated event with the same id.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/update")
	public String updateEvent (@ModelAttribute Event event, ModelMap model) {
		var updatedEvent = facade.updateEvent(event);
		model.addAttribute("updatedEvent", updatedEvent);
		return EVENT_VIEW_NAME;
	}

	/**
	 * Deletes an event by id. Adds a boolean to model data with information if deletion was successful or not.
	 *
	 * @param id Id of the event to be deleted.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/delete")
	public String deleteEvent(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.deleteEvent(id);
		model.addAttribute("eventDeleted", deleteSuccessful);
		return EVENT_VIEW_NAME;
	}
}
