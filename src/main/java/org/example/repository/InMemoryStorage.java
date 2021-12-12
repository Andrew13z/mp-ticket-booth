package org.example.repository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract storage of entities.
 */
public abstract class InMemoryStorage<T> {

	protected AtomicLong index = new AtomicLong();

	/**
	 * Provides the next index for entity.
	 * @return index
	 */
	public long getIndex() {
		return index.incrementAndGet();
	}

	/**
	 * Gets the map with entities mapped by entity id.
	 * @return Map of entities mapped by entity id
	 */
	public abstract Map<Long, T> getData();
}
