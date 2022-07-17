package org.example.controller;

import org.example.dto.UserDto;
import org.example.service.AccountService;
import org.example.service.UserService;
import org.example.validation.group.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * RestController for all operations on Users.
 *
 * @author Andrii Krokhta
 */
@RestController
@RequestMapping(value = "/users")
@Validated
public class UserController {

	private final UserService userService;
	private final AccountService accountService;

	@Autowired
	public UserController(UserService userService, AccountService accountService) {
		this.userService = userService;
		this.accountService = accountService;
	}

	/**
	 * Creates a new user.
	 *
	 * @param user New user data.
	 * @return Created user.
	 */
	@PostMapping
	@Validated(OnCreate.class)
	public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto user) {
		var createdUser = userService.createUser(user);
		accountService.createAccount(createdUser.getId());
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	/**
	 * Gets a user by id.
	 *
	 * @param id User id.
	 * @return User found by id, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	/**
	 * Gets a user by email.
	 *
	 * @param email User email.
	 * @return User found by email, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/byEmail")
	public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
		return ResponseEntity.ok(userService.getUserByEmail(email));
	}

	/**
	 * Gets a list of users by name. Name is matched using 'contains' approach.
	 *
	 * @param name     User name.
	 * @param pageable Pageable.
	 * @return List of users, or if none is found, empty list.
	 */
	@GetMapping("/byName")
	public Page<UserDto> getUsersByName(@RequestParam("name") String name, Pageable pageable) {
		return userService.getUsersByName(name, pageable);
	}

	/**
	 * Updates a user by user id.
	 *
	 * @param id   User id.
	 * @param user Updated user.
	 * @return Updated user.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserDto user) {
		return ResponseEntity.ok(userService.updateUser(id, user));
	}

	/**
	 * Deletes a user by id.
	 *
	 * @param id Id of the user to be deleted.
	 */
	@DeleteMapping
	public void deleteUser(@RequestBody Long id) {
		userService.deleteUser(id);
	}
}
