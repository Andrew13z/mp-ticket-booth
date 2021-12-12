package org.example.service.impl;

import org.example.dao.EventRepository;
import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.example.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository repository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Event getEventById(long eventId) {
		return repository.get(eventId)
					.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + eventId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return repository.getEventsByTitle(title, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return repository.getEventsForDay(day, pageSize, pageNum);
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
	public Event updateEvent(Event event) {
		return repository.updateEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEvent(long eventId) {
		return repository.delete(eventId);
	}
}
