package org.example.repository;

import org.example.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>{

	List<Event> findEventsByTitleContainingIgnoreCase(String title, Pageable pageable);

	List<Event> findEventsByDate(LocalDate date, Pageable pageable);

}
