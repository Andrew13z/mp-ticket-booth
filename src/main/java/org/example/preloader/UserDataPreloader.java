package org.example.preloader;

import org.example.converter.XmlConverter;
import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data preloader for User.
 */
@Component
public class UserDataPreloader implements DataPreloader<User>{

	private static final Logger logger = LoggerFactory.getLogger(UserDataPreloader.class);

	@Value("${users.source}")
	private Resource usersFile;

	private final XmlConverter<User> xmlConverter;
	private final UserService userService;

	@Autowired
	public UserDataPreloader(XmlConverter<User> xmlConverter, UserService userService) {
		this.xmlConverter = xmlConverter;
		this.userService = userService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> preloadData() {
		List<User> users = new ArrayList<>();
		try {
			users = xmlConverter.parseXmlToObjectList(usersFile.getFile());
		} catch (IOException e) {
			logger.warn("Failed to load user data.");
			e.printStackTrace();
		}
		users.forEach(userService::createUser);
		logger.info("Loaded user data with {} entries.", users.size());
		return users;
	}
}
