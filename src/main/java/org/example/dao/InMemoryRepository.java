package org.example.dao;

import org.example.repository.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<K, V> implements Repository<K, V> {

	protected InMemoryStorage storage;

	@Autowired
	public void setStorage(InMemoryStorage storage) {
		this.storage = storage;
	}

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
