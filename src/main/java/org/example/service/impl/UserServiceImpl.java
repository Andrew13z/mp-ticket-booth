package org.example.service.impl;

import org.example.dao.UserRepository;
import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

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
		return repository.get(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUserByEmail(String email) {
		return repository.getUserByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return repository.getUsersByName(name, pageSize, pageNum);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User createUser(User user) {
		return repository.save(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User updateUser(User user) {
		return repository.update(user);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUser(long userId) {
		return repository.delete(userId);
	}
}
