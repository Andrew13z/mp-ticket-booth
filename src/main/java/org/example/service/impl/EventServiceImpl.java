package org.example.service.impl;

import org.example.dto.EventDto;
import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.example.repository.EventRepository;
import org.example.service.EventService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	private final EventRepository repository;

	private final ModelMapper mapper;

	@Autowired
	public EventServiceImpl(EventRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EventDto getEventById(Long eventId) {
		var event = repository.findByIdWithCache(eventId)
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + eventId));
		return mapper.map(event, EventDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsByTitle(String title, int pageSize, int pageNum) {
		return mapper.map(repository.findEventsByTitleContainingIgnoreCase(title, PageRequest.of(pageNum, pageSize)),
				new TypeToken<List<EventDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsForDay(LocalDate day, int pageSize, int pageNum) {
		return mapper.map(repository.findEventsByDate(day, PageRequest.of(pageNum, pageSize)),
				new TypeToken<List<EventDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EventDto createEvent(EventDto eventDto) {
		return mapper.map(repository.save(mapper.map(eventDto, Event.class)), EventDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EventDto updateEvent(EventDto updatedEvent) {
		var oldEvent = repository.findById(updatedEvent.getId())
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + updatedEvent.getId()));
		if (!updatedEvent.getTitle().isEmpty()) {
			oldEvent.setTitle(updatedEvent.getTitle());
		}
		if (updatedEvent.getDate() != null) {
			oldEvent.setDate(updatedEvent.getDate());
		}
		if (updatedEvent.getTicketPrice() != null) {
			oldEvent.setTicketPrice(updatedEvent.getTicketPrice());
		}
		logger.info("Updated event with id {}.", updatedEvent.getId());
		return mapper.map(repository.save(mapper.map(oldEvent, Event.class)), EventDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEvent(long eventId) {
		repository.deleteById(eventId);
	}
}
