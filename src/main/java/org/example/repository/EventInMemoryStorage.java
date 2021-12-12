package org.example.repository;

import org.example.model.Event;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventInMemoryStorage extends InMemoryStorage<Event>{

	private Map<Long, Event> events = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, Event> getData() {
		return events;
	}

}
