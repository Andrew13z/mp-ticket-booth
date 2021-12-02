package org.example.dao;

import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class EventRepository extends InMemoryRepository<Long, Event> {

	private static final Logger logger = LoggerFactory.getLogger(EventRepository.class);

	@Override
	public Map<Long, Event> getData() {
		return storage.getEvents();
	}

	@Override
	public Event save(Event event) {
		var index = storage.getEventIndex();
		event.setId(index);
		getData().put(index, event);
		logger.info("Saved event with id {}.", index);
		return event;
	}

	public Event updateEvent(Event updatedEvent) {
		Event event = get(updatedEvent.getId())
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + updatedEvent.getId()));
		event.setTitle(updatedEvent.getTitle());
		event.setDate(updatedEvent.getDate());
		logger.info("Saved event with id {}.", updatedEvent.getId());
		return event;
	}

	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(event -> title.equals(event.getTitle()))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(event -> day.equals(event.getDate()))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
