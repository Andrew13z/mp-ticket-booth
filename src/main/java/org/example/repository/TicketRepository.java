package org.example.repository;

import org.example.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

	/**
	 * Get list of tickets by specified user id.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param userId   User id.
	 * @param pageable Pageable.
	 * @return List of tickets.
	 */
	@Query("{'userId': ?0}")
	Page<Ticket> findByUserId(String userId, Pageable pageable);

	/**
	 * Get list of tickets by specified event id.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param eventId   Event id.
	 * @param pageable Pageable.
	 * @return List of tickets.
	 */
	@Query("{'eventId': ?0}")
	Page<Ticket> findByEventId(String eventId, Pageable pageable);
}
