package org.example.repository.cache.impl;

import org.example.entity.Event;
import org.example.repository.cache.EventCacheRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class EventCacheRepositoryImpl implements EventCacheRepository {

	@PersistenceContext
	Session session;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Event> findByIdWithCache(Long id) {
		try {
			return Optional.of(session.createQuery("select e from Event e where e.id = :id", Event.class)
					.setParameter("id", id)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult());
		} catch (NoResultException e){
			return Optional.empty();
		}
	}
}
