package org.example.dao;

import java.util.Collection;
import java.util.Optional;

public interface Repository<K, V> {

	/**
	 * Saves the provided entity.
	 */
	V save(V value);

	/**
	 * Gets the entity by key.
	 */
	Optional<V> get(K key);

	/**
	 * Gets all saved entities.
	 */
	Collection<V> getAll();

	/**
	 * Deletes the entity by key.
	 * @param key Entity id
	 * @return Boolean with information if deletion was successful or not.
	 */
	boolean delete(K key);
}
