package org.example.dao;

import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.repository.InMemoryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository extends InMemoryRepository<Long, User>{

	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	private final InMemoryStorage<User> storage;

	@Value("${users.source}")
	private String usersFile;

	@Autowired
	public UserRepository(InMemoryStorage<User> storage) {
		this.storage = storage;
	}

	@Override
	public Map<Long, User> getData() {
		return storage.getData();
	}

	@Override
	public User save(User user) {
		if (isEmailUnique(user.getEmail())) {
			var index = storage.getIndex();
			user.setId(index);
			getData().put(index, user);
			logger.info("Saved user with id {}.", index);
			return user;
		}
		logger.error("Failed to create user. Email: {} is already taken.", user.getEmail());
		throw new IllegalArgumentException("User email must be unique");
	}

	public User update(User updatedUser) {
		User oldUser = get(updatedUser.getId())
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + updatedUser.getId()));

		var email = updatedUser.getEmail();
		if (!email.isEmpty() && !oldUser.getEmail().equals(email)){
			if (isEmailUnique(email)){
				oldUser.setEmail(email);
			} else {
				logger.error("Failed to create user. Email: {} is already taken.", email);
				throw new IllegalArgumentException("User email must be unique");
			}
		}

		if (!updatedUser.getName().isEmpty()) {
			oldUser.setName(updatedUser.getName());
		}

		logger.info("Updated user with id {}.", updatedUser.getId());

		return oldUser;
	}

	private boolean isEmailUnique(String email) {
		return getAll().stream()
				.map(User::getEmail)
				.noneMatch(email::equals);
	}

	public Optional<User> getUserByEmail(String email) {
		return getAll().stream()
				.filter(user -> email.equals(user.getEmail()))
				.findAny();
	}

	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return getAll().stream()
				.filter(user -> user.getName().contains(name))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
