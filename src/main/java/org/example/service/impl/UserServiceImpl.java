package org.example.service.impl;

import org.example.model.User;
import org.example.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

	@Override
	public User getUserById(long userId) {
		return null;
	}

	@Override
	public User getUserByEmail(String email) {
		return null;
	}

	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return null;
	}

	@Override
	public User createUser(User user) {
		return null;
	}

	@Override
	public User updateUser(User user) {
		return null;
	}

	@Override
	public boolean deleteUser(long userId) {
		return false;
	}
}
