package org.example.repository;

import org.example.model.Event;
import org.example.repository.cache.EventCacheRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, EventCacheRepository {

	/**
	 * Get list of events by matching title. Title is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param title    Event title or it's part.
	 * @param pageable Pageable.
	 * @return List of events.
	 */
	Page<Event> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable);

	/**
	 * Get list of events for specified date.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param date     Date object from which day information is extracted.
	 * @param pageable Pageable.
	 * @return List of events.
	 */
	Page<Event> findEventsByDate(LocalDate date, Pageable pageable);

}
