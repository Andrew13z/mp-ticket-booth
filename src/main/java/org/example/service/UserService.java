package org.example.service;

import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.exception.EntityNotFoundException;
import org.example.exception.UserBalanceException;
import org.example.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository repository;

	private final ModelMapper mapper;

	@Autowired
	public UserService(UserRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * Gets user by its id.
	 *
	 * @return User.
	 */
	@Cacheable("users")
	public UserDto getUserById(String userId) {
		var user = repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
		return mapper.map(user, UserDto.class);
	}

	/**
	 * Gets user by its email. Email is strictly matched.
	 *
	 * @return User.
	 */
	public UserDto getUserByEmail(String email) {
		var user = repository.findByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
		return mapper.map(user, UserDto.class);
	}

	/**
	 * Get list of users by matching name. Name is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param name     Users name or it's part.
	 * @param pageable Pageable
	 * @return List of users.
	 */
	public Page<UserDto> getUsersByName(String name, Pageable pageable) {
		var users = repository.findUsersByNameContainingIgnoreCase(name, pageable);
		return mapper.map(users, new TypeToken<Page<UserDto>>(){}.getType());
	}

	/**
	 * Creates new user. User id is be auto-generated.
	 *
	 * @param userDto User data.
	 * @return Created User object.
	 */
	public UserDto createUser(UserDto userDto) {
		if (repository.findByEmail(userDto.getEmail()).isEmpty()) {
			var user = mapper.map(userDto, User.class);
			user.setBalance(BigDecimal.ZERO);
			return mapper.map(repository.save(user), UserDto.class);
		}
		logger.error("Failed to create user. User with email: {} already exists.", userDto.getEmail());
		throw new IllegalArgumentException("User email must be unique");
	}

	/**
	 * Updates user using given data.
	 *
	 * @param id Id of the user to be  updated.
	 * @param user User data for update. Should have id set.
	 * @return Updated User object.
	 */
	public UserDto updateUser(String id, UserDto updatedUserDto) {
		var oldUser = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));

		var email = updatedUserDto.getEmail();
		if (!oldUser.getEmail().equals(email)) {
			if (repository.findByEmail(email).isEmpty()) {
				oldUser.setEmail(email);
			} else {
				logger.error("Failed to update user. User with email: {} already exists.", email);
				throw new IllegalArgumentException("User email must be unique");
			}
		}

		oldUser.setName(updatedUserDto.getName());

		logger.info("Updated user with id {}.", id);
		return mapper.map(repository.save(oldUser), UserDto.class);
	}

	/**
	 * Adds the provided refill sum to the specified user's balbance.
	 *
	 * @param userId User id.
	 * @param refillSum Sum to be added to the balance.
	 * @return User object with updated balance.
	 */
	public UserDto refillBalance(String userId, BigDecimal refillSum) {
		var account = repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));
		var balance = account.getBalance();
		account.setBalance(balance.add(refillSum));
		logger.info("Refilling user balance (id: {}) by {}.", userId, refillSum);
		return mapper.map(repository.save(account), UserDto.class);
	}

	/**
	 * Subtracts the provided ticket price from the specified account by id.
	 *
	 * @param userId User id.
	 * @param ticketPrice Amount to be subtracted from the balance.
	 * @return User object with updated balance.
	 */
	public UserDto chargeForTicket(String userId, BigDecimal ticketPrice) {
		var account = repository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found by id: " + userId));

		var accountBalance = account.getBalance();
		if (accountBalance.compareTo(ticketPrice) < 0) {
			logger.warn("User {} has insufficient funds to buy the ticket with price {}.", userId, ticketPrice);
			throw new UserBalanceException("User has insufficient funds.");
		}
		account.setBalance(accountBalance.subtract(ticketPrice));
		logger.info("Charged user (id: {}) for {}.", userId, ticketPrice);
		return mapper.map(repository.save(account), UserDto.class);
	}

	/**
	 * Deletes user by its id.
	 *
	 * @param userId User id.
	 */
	public void deleteUser(String userId) {
		repository.deleteById(userId);
	}
}
