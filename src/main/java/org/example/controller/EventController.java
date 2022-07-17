package org.example.controller;

import org.example.dto.EventDto;
import org.example.service.EventService;
import org.example.validation.group.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * RestController for all operations on Events.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping(value = "/events")
@Validated
public class EventController {

	private final EventService eventService;

	@Autowired
	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	/**
	 * Creates a new event.
	 *
	 * @param event New event data.
	 * @return Created event.
	 */
	@PostMapping
	@Validated(OnCreate.class)
	public ResponseEntity<EventDto> createEvent(@RequestBody @Valid EventDto event) {
		var createdEvent = eventService.createEvent(event);
		return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
	}

	/**
	 * Gets an event by id.
	 *
	 * @param id Event id.
	 * @return Event found by id, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(eventService.getEventById(id));
	}

	/**
	 * Gets a list of events by title.
	 *
	 * @param title    Event title.
	 * @param pageable Pageable.
	 * @return List of events, or if none is found, empty list.
	 */
	@GetMapping
	public Page<EventDto> getEventsByTitle(@RequestParam(value = "title", required = false) String title,
										   @RequestParam(value = "date", required = false)
										   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
										   Pageable pageable) {
		if (title != null) {
			return eventService.getEventsByTitle(title, pageable);
		}
		if (date != null) {
			return eventService.getEventsForDay(date, pageable);
		}
		throw new IllegalArgumentException("No parameters provided to search by.");
	}

	/**
	 * Updates an event by event id.
	 *
	 * @param id    EventId.
	 * @param event Updated event with the same id.
	 * @return Updated Event.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<EventDto> updateEvent(@PathVariable("id") Long id, @RequestBody EventDto event) {
		return ResponseEntity.ok(eventService.updateEvent(id, event));
	}

	/**
	 * Deletes an event by id.
	 *
	 * @param id Id of the event to be deleted.
	 */
	@DeleteMapping
	public void deleteEvent(@RequestBody Long id) {
		eventService.deleteEvent(id);
	}
}
