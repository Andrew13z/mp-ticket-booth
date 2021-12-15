package org.example.preloader;

import org.example.converter.XmlConverter;
import org.example.model.Event;
import org.example.service.EventService;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data preloader for Event.
 */
@Component
public class EventDataPreloader implements DataPreloader<Event>{

	private static final Logger logger = LoggerFactory.getLogger(EventDataPreloader.class);

	@Value("${events.source}")
	private Resource eventsFile;

	private final XmlConverter<Event> xmlConverter;
	private final EventService eventService;

	@Autowired
	public EventDataPreloader(XmlConverter<Event> xmlConverter, EventService eventService) {
		this.xmlConverter = xmlConverter;
		this.eventService = eventService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> preloadData() {
		List<Event> events = new ArrayList<>();
		try {
			events = xmlConverter.parseXmlToObjectList(eventsFile.getFile());
		} catch (IOException e) {
			logger.warn("Failed to load event data.");
			e.printStackTrace();
		}
		events.forEach(eventService::createEvent);
		logger.info("Loaded event data with {} entries.", events.size());
		return events;
	}
}
