package org.example.controller;

import org.example.dto.UserDto;
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
import javax.validation.constraints.Min;
import java.math.BigDecimal;

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

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
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
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	/**
	 * Gets a user by id.
	 *
	 * @param id User id.
	 * @return User found by id, otherwise throws EntityNotFoundException.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
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
	public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody @Valid UserDto user) {
		return ResponseEntity.ok(userService.updateUser(id, user));
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<UserDto> refillAccount(@PathVariable("id") String id,
												 @RequestBody @Min(0) BigDecimal refillSum) {
		return ResponseEntity.ok(userService.refillBalance(id, refillSum));
	}

	/**
	 * Deletes a user by id.
	 *
	 * @param id Id of the user to be deleted.
	 */
	@DeleteMapping
	public void deleteUser(@RequestBody String id) {
		userService.deleteUser(id);
	}
}
