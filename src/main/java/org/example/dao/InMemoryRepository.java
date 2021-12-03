package org.example.dao;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<K, V> implements Repository<K, V> {

	public abstract Map<K, V> getData();

	@Override
	public Optional<V> get(K key) {
		return Optional.ofNullable(getData().get(key));
	}

	public Collection<V> getAll() {
		return getData().values();
	}

	public boolean delete(K key) {
		return getData().remove(key) != null;
	}
}
