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

	@Override
	public Event getEventById(long eventId) {
		return repository.get(eventId)
					.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + eventId));
	}

	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return repository.getEventsByTitle(title, pageSize, pageNum);
	}

	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return repository.getEventsForDay(day, pageSize, pageNum);
	}

	@Override
	public Event createEvent(Event event) {
		return repository.save(event);
	}

	@Override
	public Event updateEvent(Event event) {
		return repository.updateEvent(event);
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return repository.delete(eventId);
	}
}
