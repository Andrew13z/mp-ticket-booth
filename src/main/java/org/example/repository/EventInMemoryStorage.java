package org.example.repository;

import org.example.model.Event;
import org.example.repository.parser.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventInMemoryStorage extends InMemoryStorage<Event>{

	private static final Logger logger = LoggerFactory.getLogger(EventInMemoryStorage.class);

	@Value("${events.source}")
	private Resource eventFile;

	private Map<Long, Event> events = new HashMap<>();

	@Autowired
	public EventInMemoryStorage(AbstractParser<Event> parser) {
		super(parser);
	}

	@PostConstruct
	private void postConstruct() {
		try {
			events = parser.loadData(eventFile.getFile().getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		index = new AtomicLong(events.size());
		logger.info("Loaded event data with {} entries.", events.size());
	}

	@Override
	public Map<Long, Event> getData() {
		return events;
	}

}
