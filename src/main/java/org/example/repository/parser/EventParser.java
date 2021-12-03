package org.example.repository.parser;

import org.example.model.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EventParser extends AbstractParser<Event>{

	@Override
	public Event parse(String content) {
		var columns = content.split(",");
		return new Event(Long.parseLong(columns[0]), columns[1], LocalDate.parse(columns[2]));
	}

	@Override
	public long getId(Event model) {
		return model.getId();
	}

	@Override
	public Class<Event> getType() {
		return Event.class;
	}
}
