package org.example.service;

import org.example.dto.EventDto;
import org.example.exception.EntityNotFoundException;
import org.example.entity.Event;
import org.example.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventService.class);

	private final EventRepository repository;

	private final ModelMapper mapper;

	@Autowired
	public EventService(EventRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Gets event by its id.
	 *
	 * @return Event.
	 */
	@Cacheable("events")
	public EventDto getEventById(String eventId) {
		var event = repository.findById(eventId)
			.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + eventId));
		return mapper.map(event, EventDto.class);
	}

	/**
	 * Get list of events by matching title. Title is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param title    Event title or it's part.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	public Page<EventDto> getEventsByTitle(String title, Pageable pageable) {
		return mapper.map(repository.findEventsByTitleContainingIgnoreCase(title, pageable),
			new TypeToken<Page<EventDto>>(){}.getType());
	}

	/**
	 * Get list of events for specified day.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param day      Date object from which day information is extracted.
	 * @param pageable Pageable
	 * @return List of events.
	 */
	public Page<EventDto> getEventsForDay(LocalDate day, Pageable pageable) {
		return mapper.map(repository.findEventsByDate(day, pageable),
			new TypeToken<Page<EventDto>>(){}.getType());
	}

	/**
	 * Creates new event. Event id is be auto-generated.
	 *
	 * @param eventDto Event data.
	 * @return Created Event object.
	 */
	public EventDto createEvent(EventDto eventDto) {
		return mapper.map(repository.save(mapper.map(eventDto, Event.class)), EventDto.class);
	}

	/**
	 * Updates event using given data.
	 *
	 * @param id Id of the event to be  updated.
	 * @param updatedEvent Event data for update. Should have id set.
	 * @return Updated Event object.
	 */
	public EventDto updateEvent(String id, EventDto updatedEvent) {
		var oldEvent = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + id));

		oldEvent.setTitle(updatedEvent.getTitle());
		oldEvent.setDate(updatedEvent.getDate());
		oldEvent.setTicketPrice(updatedEvent.getTicketPrice());

		logger.info("Updated event with id {}.", id);
		return mapper.map(repository.save(oldEvent), EventDto.class);
	}

	/**
	 * Deletes event by its id.
	 *
	 * @param eventId Event id.
	 */
	public void deleteEvent(String eventId) {
		repository.deleteById(eventId);
	}
}
