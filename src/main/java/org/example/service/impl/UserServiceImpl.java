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

	@Autowired
	private UserRepository repository;

	@Override
	public User getUserById(long userId) {
		return repository.get(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
	}

	@Override
	public User getUserByEmail(String email) {
		return repository.getUserByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
	}

	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return repository.getUsersByName(name, pageSize, pageNum);
	}

	@Override
	public User createUser(User user) {
		return repository.save(user);
	}

	@Override
	public User updateUser(User user) {
		return repository.update(user);
	}

	@Override
	public boolean deleteUser(long userId) {
		return repository.delete(userId);
	}
}
