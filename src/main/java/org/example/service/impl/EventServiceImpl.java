package org.example.service.impl;

import org.example.model.Event;
import org.example.service.EventService;

import java.time.LocalDate;
import java.util.List;

public class EventServiceImpl implements EventService {

	@Override
	public Event getEventById(long eventId) {
		return null;
	}

	@Override
	public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
		return null;
	}

	@Override
	public List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return null;
	}

	@Override
	public Event createEvent(Event event) {
		return null;
	}

	@Override
	public Event updateEvent(Event event) {
		return null;
	}

	@Override
	public boolean deleteEvent(long eventId) {
		return false;
	}
}
