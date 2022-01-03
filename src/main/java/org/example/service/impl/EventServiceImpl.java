package org.example.service.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.example.repository.EventRepository;
import org.example.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventRepository repository;

	@Autowired
	public EventServiceImpl(EventRepository repository) {
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getEventById(long eventId) {
		return repository.findById(eventId)
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + eventId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return repository.findEventsByTitleContainingIgnoreCase(title, PageRequest.of(pageNum, pageSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return repository.findEventsByDate(day, PageRequest.of(pageNum, pageSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event createEvent(Event event) {
		return repository.save(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event updateEvent(Event updatedEvent) {
		var oldEvent = repository.findById(updatedEvent.getId())
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + updatedEvent.getId()));
		if (!updatedEvent.getTitle().isEmpty()) {
			oldEvent.setTitle(updatedEvent.getTitle());
		}
		if (updatedEvent.getDate() != null) {
			oldEvent.setDate(updatedEvent.getDate());
		}
		if (updatedEvent.getTicketPrice() != null) {
			oldEvent.setTicketPrice(updatedEvent.getTicketPrice());
		}
		logger.info("Updated event with id {}.", updatedEvent.getId());
		return repository.save(oldEvent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEvent(long eventId) {
		repository.deleteById(eventId);
	}
}
