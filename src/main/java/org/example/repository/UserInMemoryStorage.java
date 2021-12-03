package org.example.repository;

import org.example.model.User;
import org.example.repository.parser.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserInMemoryStorage extends InMemoryStorage<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserInMemoryStorage.class);

	@Value("${users.source}")
	private String usersFileName;

	private Map<Long, User> users;

	@Autowired
	public UserInMemoryStorage(AbstractParser<User> parser) {
		super(parser);
	}

	@PostConstruct
	private void postConstruct() {
		users = parser.loadData(usersFileName);
		index = new AtomicLong(users.size());
		logger.info("Loaded user data with {} entries.", users.size());
	}

	@Override
	public Map<Long, User> getData() {
		return users;
	}


}
