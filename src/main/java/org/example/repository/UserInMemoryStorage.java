package org.example.repository;

import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserInMemoryStorage extends InMemoryStorage<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserInMemoryStorage.class);

	private Map<Long, User> users = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Long, User> getData() {
		return users;
	}

}
