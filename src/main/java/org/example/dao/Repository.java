package org.example.dao;

import java.util.Collection;
import java.util.Optional;

public interface Repository<K, V> {

	V save(V value);

	Optional<V> get(K key);

	Collection<V> getAll();

	boolean delete(K key);
}
