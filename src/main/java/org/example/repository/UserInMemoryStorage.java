package org.example.repository;

import org.example.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserInMemoryStorage extends InMemoryStorage<User> {

	private final Map<Long, User> users = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, User> getData() {
		return users;
	}
}
