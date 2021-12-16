package org.example.dao;

import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.example.repository.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository for all operations on Events.
 * @author Andrii Krokhta
 */
@Repository
public class EventRepository extends InMemoryRepository<Long, Event> {

	private static final Logger logger = LoggerFactory.getLogger(EventRepository.class);

	private final InMemoryStorage<Event> storage;

	@Autowired
	public EventRepository(InMemoryStorage<Event> storage) {
		this.storage = storage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, Event> getData() {
		return storage.getData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event save(Event event) {
		var index = storage.getIndex();
		event.setId(index);
		getData().put(index, event);
		logger.info("Saved event with id {}.", index);
		return event;
	}

	/**
	 * Updates an event by event id.
	 *
	 * @param updatedEvent Updated event with the same id.
	 * @return Updated event.
	 */
	public Event updateEvent(Event updatedEvent) {
		Event event = get(updatedEvent.getId())
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + updatedEvent.getId()));
		if (!updatedEvent.getTitle().isEmpty()) {
			event.setTitle(updatedEvent.getTitle());
		}
		if (updatedEvent.getDate() != null) {
			event.setDate(updatedEvent.getDate());
		}
		logger.info("Updated event with id {}.", updatedEvent.getId());
		return event;
	}

	/**
	 * Gets a list of events by title.
	 *
	 * @param title Event title.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @return List of events or empty list if no events for the provided title are found.
	 */
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(event -> title.equals(event.getTitle()))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	/**
	 * Gets a list of events by date.
	 *
	 * @param day Event date.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @return List of events or empty list if no events for the provided date are found.
	 */
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(event -> day.equals(event.getDate()))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
