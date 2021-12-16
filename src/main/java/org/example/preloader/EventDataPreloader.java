package org.example.preloader;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
import org.example.model.Event;
import org.example.service.EventService;
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

	private final XmlMarshaller xmlMarshaller;
	private final EventService eventService;

	@Autowired
	public EventDataPreloader(XmlMarshaller xmlMarshaller, EventService eventService) {
		this.xmlMarshaller = xmlMarshaller;
		this.eventService = eventService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Event> preloadData() {
		List<Event> events = new ArrayList<>();
		try {
			events = xmlMarshaller.parse(eventsFile, new TypeReference<>(){});
		} catch (IOException e) {
			logger.warn("Failed to load event data: {}", e.getMessage());
			e.printStackTrace();
		}
		events.forEach(eventService::createEvent);
		logger.info("Loaded event data with {} entries.", events.size());
		return events;
	}
}
