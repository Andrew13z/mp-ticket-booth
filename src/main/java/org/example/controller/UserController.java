package org.example.controller;

import org.example.dto.UserDto;
import org.example.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RestController for all operations on Users.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private final BookingFacade facade;

	@Autowired
	public UserController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new user.
	 *
	 * @param user  New user data.
	 * @return Created user.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@RequestBody UserDto user) {
		var createdUser = facade.createUser(user);
		facade.createAccount(createdUser.getId());
		return createdUser;
	}

	/**
	 * Gets a user by id.
	 *
	 * @param id    User id.
	 * @return User found by id, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable("id") Long id) {
		return facade.getUserById(id);
	}

	/**
	 * Gets a user by email.
	 *
	 * @param email User email.
	 * @return User found by email, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/byEmail")
	public UserDto getUserByEmail(@RequestParam("email") String email) {
		return facade.getUserByEmail(email);
	}

	/**
	 * Gets a list of users by name. Name is matched using 'contains' approach.
	 *
	 * @param name     User name.
	 * @param pageable Pageable.
	 * @return List of users, or if none is found, empty list.
	 */
	@GetMapping("/byName")
	public List<UserDto> getUsersByName(@RequestParam("name") String name, Pageable pageable) {
		return facade.getUsersByName(name, pageable);
	}

	/**
	 * Updates a user by user id.
	 *
	 * @param id User id.
	 * @param user  Updated user.
	 * @return Updated user.
	 */
	@PutMapping("/{id}")
	public UserDto updateUser(@PathVariable("id") Long id, @RequestBody UserDto user, ModelMap model) {
		return facade.updateUser(id, user);
	}

	/**
	 * Deletes a user by id.
	 * @param id    Id of the user to be deleted.
	 */
	@DeleteMapping
	@Transactional
	public void deleteUser(@RequestBody Long id) {
		facade.deleteAccount(id);
		facade.deleteUser(id);
	}
}
