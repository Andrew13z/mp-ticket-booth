package org.example.preloader;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
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

	private final XmlMarshaller xmlMarshaller;
	private final UserService userService;

	@Autowired
	public UserDataPreloader(XmlMarshaller xmlMarshaller, UserService userService) {
		this.xmlMarshaller = xmlMarshaller;
		this.userService = userService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> preloadData() {
		List<User> users = new ArrayList<>();
		try {
			users = xmlMarshaller.parse(usersFile, new TypeReference<>(){});
		} catch (IOException e) {
			logger.warn("Failed to load user data: {}", e.getMessage());
			e.printStackTrace();
		}
		users.forEach(userService::createUser);
		logger.info("Loaded user data with {} entries.", users.size());
		return users;
	}
}
