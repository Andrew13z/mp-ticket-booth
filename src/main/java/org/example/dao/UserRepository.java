package org.example.dao;

import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository extends InMemoryRepository<Long, User>{

	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	@Override
	public Map<Long, User> getData() {
		return storage.getUsers();
	}

	@Override
	public User save(User user) {
		if (isEmailUnique(user.getEmail())) {
			var index = storage.getUserIndex();
			user.setId(index);
			getData().put(index, user);
			logger.info("Saved user with id {}.", index);
			return user;
		}
		logger.warn("Failed to create user. Email: {} is already taken.", user.getEmail());
		throw new IllegalArgumentException("User email must be unique");
	}

	public User update(User updatedUser) {
		User oldUser = get(updatedUser.getId())
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + updatedUser.getId()));

		var email = updatedUser.getEmail();
		if (!oldUser.getEmail().equals(email)){
			if (isEmailUnique(email)){
				oldUser.setEmail(email);
			} else {
				logger.warn("Failed to create user. Email: {} is already taken.", email);
				throw new IllegalArgumentException("User email must be unique");
			}
		}

		oldUser.setName(updatedUser.getName());
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
				.filter(user -> name.equals(user.getName()))
				.skip(pageSize * (pageNum - 1L))
				.limit(pageSize)
				.collect(Collectors.toList());
	}
}
