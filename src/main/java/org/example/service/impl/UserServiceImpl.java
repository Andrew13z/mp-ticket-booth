package org.example.service.impl;

import org.example.dto.UserDto;
import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

	private final ModelMapper mapper;

	@Autowired
	public UserServiceImpl(UserRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto getUserById(Long userId) {
		var user = repository.findByIdWithCache(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
		return mapper.map(user, UserDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto getUserByEmail(String email) {
		var user = repository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
		return mapper.map(user, UserDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserDto> getUsersByName(String name, int pageSize, int pageNum) {
		var users = repository.findUsersByNameContainingIgnoreCase(name, PageRequest.of(pageNum, pageSize));
		return mapper.map(users, new TypeToken<List<UserDto>>(){}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto createUser(UserDto userDto) {
		if (repository.findByEmail(userDto.getEmail()).isEmpty()) {
			var user = mapper.map(userDto, User.class);
			return mapper.map(repository.save(user), UserDto.class);
		}
		logger.error("Failed to create user. User with email: {} already exists.", userDto.getEmail());
		throw new IllegalArgumentException("User email must be unique");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDto updateUser(UserDto updatedUserDto) {
		var oldUser = repository.findById(updatedUserDto.getId())
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + updatedUserDto.getId()));

		var email = updatedUserDto.getEmail();
		if (!email.isEmpty() && !oldUser.getEmail().equals(email)) {
			if (repository.findByEmail(email).isEmpty()) {
				oldUser.setEmail(email);
			} else {
				logger.error("Failed to update user. User with email: {} already exists.", email);
				throw new IllegalArgumentException("User email must be unique");
			}
		}

		if (!updatedUserDto.getName().isEmpty()) {
			oldUser.setName(updatedUserDto.getName());
		}

		logger.info("Updated user with id {}.", updatedUserDto.getId());

		return mapper.map(repository.save(oldUser), UserDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteUser(Long userId) {
		repository.deleteById(userId);
	}
}
