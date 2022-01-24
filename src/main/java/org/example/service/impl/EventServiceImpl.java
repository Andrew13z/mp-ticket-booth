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
import org.springframework.data.domain.Pageable;
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
	public List<EventDto> getEventsByTitle(String title, Pageable pageable) {
		return mapper.map(repository.findEventsByTitleContainingIgnoreCase(title, pageable),
				new TypeToken<List<EventDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EventDto> getEventsForDay(LocalDate day, Pageable pageable) {
		return mapper.map(repository.findEventsByDate(day, pageable),
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
	public EventDto updateEvent(Long id, EventDto updatedEvent) {
		var oldEvent = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Event not found by id: " + id));

		oldEvent.setTitle(updatedEvent.getTitle());
		oldEvent.setDate(updatedEvent.getDate());
		oldEvent.setTicketPrice(updatedEvent.getTicketPrice());

		logger.info("Updated event with id {}.", id);
		return mapper.map(repository.save(oldEvent), EventDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEvent(Long eventId) {
		repository.deleteById(eventId);//todo is event lookup required to check if event exists
	}
}
