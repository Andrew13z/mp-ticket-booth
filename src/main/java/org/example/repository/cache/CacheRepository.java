package org.example.repository.cache;

import java.util.Optional;

public interface CacheRepository<T> {

	/**
	 *	Gets entity by id with caching enabled.
	 * @param id Entity id
	 * @return Optional of entity or empty optional if not found
	 */
	Optional<T> findByIdWithCache(Long id);
}
