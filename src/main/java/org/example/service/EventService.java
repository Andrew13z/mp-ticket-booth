package org.example.service;

import org.example.dto.EventDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

	/**
	 * Gets event by its id.
	 *
	 * @return Event.
	 */
	EventDto getEventById(Long eventId);

	/**
	 * Get list of events by matching title. Title is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param title    Event title or it's part.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	List<EventDto> getEventsByTitle(String title, Pageable pageable);

	/**
	 * Get list of events for specified day.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param day      Date object from which day information is extracted.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	List<EventDto> getEventsForDay(LocalDate day, Pageable pageable);

	/**
	 * Creates new event. Event id is be auto-generated.
	 *
	 * @param event Event data.
	 * @return Created Event object.
	 */
	EventDto createEvent(EventDto event);

	/**
	 * Updates event using given data.
	 *
	 * @param id Id of the event to be  updated.
	 * @param event Event data for update. Should have id set.
	 * @return Updated Event object.
	 */
	EventDto updateEvent(Long id, EventDto event);

	/**
	 * Deletes event by its id.
	 *
	 * @param eventId Event id.
	 * @return Flag that shows whether event has been deleted.
	 */
	void deleteEvent(long eventId);
}
