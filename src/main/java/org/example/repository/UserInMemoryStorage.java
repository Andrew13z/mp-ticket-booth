package org.example.repository;

import org.example.model.User;
import org.example.repository.parser.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserInMemoryStorage extends InMemoryStorage<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserInMemoryStorage.class);

	@Value("${users.source}")
	private Resource usersFile;

	private Map<Long, User> users = new HashMap<>();

	@Autowired
	public UserInMemoryStorage(AbstractParser<User> parser) {
		super(parser);
	}

	@PostConstruct
	private void postConstruct() {
		try {
			users = parser.loadData(usersFile.getFile().getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		index = new AtomicLong(users.size());
		logger.info("Loaded user data with {} entries.", users.size());
	}

	@Override
	public Map<Long, User> getData() {
		return users;
	}

}
