package org.example.service.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository repository;

	@Autowired
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserById(long userId) {
		return repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserByEmail(String email) {
		return repository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return repository.findUsersByNameContainingIgnoreCase(name, PageRequest.of(pageNum, pageSize));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createUser(User user) {
		if (repository.findByEmail(user.getEmail()).isEmpty()) {
			return repository.save(user);
		}
		logger.error("Failed to create user. User with email: {} already exists.", user.getEmail());
		throw new IllegalArgumentException("User email must be unique");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User updateUser(User updatedUser) {
		var oldUser = repository.findById(updatedUser.getId())
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + updatedUser.getId()));

		var email = updatedUser.getEmail();
		if (!email.isEmpty() && !oldUser.getEmail().equals(email)) {
			if (repository.findByEmail(email).isEmpty()) {
				oldUser.setEmail(email);
			} else {
				logger.error("Failed to update user. User with email: {} already exists.", email);
				throw new IllegalArgumentException("User email must be unique");
			}
		}

		if (!updatedUser.getName().isEmpty()) {
			oldUser.setName(updatedUser.getName());
		}

		logger.info("Updated user with id {}.", updatedUser.getId());

		return repository.save(oldUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteUser(long userId) {
		repository.deleteById(userId);
	}
}
