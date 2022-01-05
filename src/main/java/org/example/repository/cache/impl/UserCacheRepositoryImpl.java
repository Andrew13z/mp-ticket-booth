package org.example.repository.cache.impl;

import org.example.model.User;
import org.example.repository.cache.UserCacheRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserCacheRepositoryImpl implements UserCacheRepository {

	@PersistenceContext
	Session session;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> findByIdWithCache(Long id) {
		try {
			return Optional.of(session.createQuery("select u from User u where u.id = :id", User.class)
					.setParameter("id", id)
					.setHint("org.hibernate.cacheable", true)
					.getSingleResult());
		} catch (NoResultException e){
			return Optional.empty();
		}
	}
}
