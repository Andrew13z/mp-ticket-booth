package org.example.service;

import org.example.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

	/**
	 * Gets user by its id.
	 *
	 * @return User.
	 */
	UserDto getUserById(Long userId);

	/**
	 * Gets user by its email. Email is strictly matched.
	 *
	 * @return User.
	 */
	UserDto getUserByEmail(String email);

	/**
	 * Get list of users by matching name. Name is matched using 'contains' approach.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param name     Users name or it's part.
	 * @param pageable Pageable
	 * @return List of users.
	 */
	List<UserDto> getUsersByName(String name, Pageable pageable);

	/**
	 * Creates new user. User id is be auto-generated.
	 *
	 * @param user User data.
	 * @return Created User object.
	 */
	UserDto createUser(UserDto user);

	/**
	 * Updates user using given data.
	 *
	 * @param id Id of the user to be  updated.
	 * @param user User data for update. Should have id set.
	 * @return Updated User object.
	 */
	UserDto updateUser(Long id, UserDto user);

	/**
	 * Deletes user by its id.
	 *
	 * @param userId User id.
	 * @return Flag that shows whether user has been deleted.
	 */
	void deleteUser(Long userId);
}
