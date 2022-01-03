package org.example.repository;

import org.example.model.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {

	@Query("select t from Ticket t where t.user.id = :userId")
	List<Ticket> findByUserId(@Param("userId") Long userId, Pageable pageable);

	@Query("select t from Ticket t where t.event.id = :eventId")
	List<Ticket> findByEventId(@Param("eventId")Long eventId, Pageable pageable);
}
