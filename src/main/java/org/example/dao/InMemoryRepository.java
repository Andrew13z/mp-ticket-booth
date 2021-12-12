package org.example.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<K, V> implements Repository<K, V> {

	/**
	 * Gets the map with entities mapped by entity id.
	 * @return Map of entities mapped by entity id
	 */
	public abstract Map<K, V> getData();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<V> get(K key) {
		return Optional.ofNullable(getData().get(key));
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<V> getAll() {
		return getData().values();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean delete(K key) {
		return getData().remove(key) != null;
	}
}
