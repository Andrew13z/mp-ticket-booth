package org.example.repository;

import org.example.model.Event;
import org.example.repository.parser.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventInMemoryStorage extends InMemoryStorage<Event>{

	private static final Logger logger = LoggerFactory.getLogger(EventInMemoryStorage.class);

//	@Value("${events.source}")
//	private String eventFileName;

	private Map<Long, Event> events = new HashMap<>();

	@Autowired
	public EventInMemoryStorage(AbstractParser<Event> parser) {
		super(parser);
	}

//	@PostConstruct
//	private void postConstruct() {
//		events = parser.loadData(eventFileName);
//		index = new AtomicLong(events.size());
//		logger.info("Loaded event data with {} entries.", events.size());
//	}

	@Override
	public Map<Long, Event> getData() {
		return events;
	}

}
