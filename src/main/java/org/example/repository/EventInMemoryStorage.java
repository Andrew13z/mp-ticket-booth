package org.example.repository;

import org.example.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventInMemoryStorage extends InMemoryStorage<Event>{

	private static final Logger logger = LoggerFactory.getLogger(EventInMemoryStorage.class);

	private Map<Long, Event> events = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, Event> getData() {
		return events;
	}

}
