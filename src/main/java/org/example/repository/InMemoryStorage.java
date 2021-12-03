package org.example.repository;

import org.example.repository.parser.AbstractParser;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryStorage<T> {

	protected final AbstractParser<T> parser;

	protected AtomicLong index = new AtomicLong();

	protected InMemoryStorage(AbstractParser<T> parser) {
		this.parser = parser;
	}

	public long getIndex() {
		return index.incrementAndGet();
	}

	public abstract Map<Long, T> getData();
}
