package org.example.controller;

import org.example.dto.EventDto;
import org.example.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * RestController for all operations on Events.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventController {

	private final BookingFacade facade;

	@Autowired
	public EventController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new event.
	 *
	 * @param event New event data.
	 * @return Created event.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventDto createEvent(@RequestBody EventDto event) {
		return facade.createEvent(event);
	}

	/**
	 * Gets an event by id.
	 *
	 * @param id    Event id.
	 * @return Event found by id, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/{id}")
	public EventDto getEventById(@PathVariable("id") Long id) {
		return facade.getEventById(id);
	}

	/**
	 * Gets a list of events by title.
	 *
	 * @param title    Event title.
	 * @param pageable Pageable.
	 * @return List of events, or if none is found, empty list.
	 */
	@GetMapping
	public List<EventDto> getEventsByTitle(@RequestParam(value = "title", required = false) String title,
										   @RequestParam(value = "date", required = false)
										   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
										   Pageable pageable) {
		if (title != null) {
			return facade.getEventsByTitle(title, pageable);
		}
		if (date != null) {
			return facade.getEventsByDate(date, pageable);
		}
		throw new IllegalArgumentException("No parameters provided to search by.");
	}

	/**
	 * Updates an event by event id.
	 *
	 * @param id EventId.
	 * @param event Updated event with the same id.
	 * @return Updated Event.
	 */
	@PutMapping("/{id}")
	public EventDto updateEvent(@PathVariable("id") Long id, @RequestBody EventDto event) {
		return facade.updateEvent(id, event);
	}

	/**
	 * Deletes an event by id.
	 * @param id    Id of the event to be deleted.
	 */
	@DeleteMapping
	public void deleteEvent(@RequestBody Long id) {
		facade.deleteEvent(id);
	}
}
