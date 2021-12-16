package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for all operations on Users.
 * @author Andrii Krokhta
 */
@Controller
@RequestMapping("/user")
public class UserController {

	public static final String USER_VIEW_NAME = "user";
	private final BookingFacade facade;

	@Autowired
	public UserController(BookingFacade facade) {
		this.facade = facade;
	}

	/**
	 * Creates a new user and adds to it model data.
	 *
	 * @param user New user data.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping
	public String createUser(@ModelAttribute User user, ModelMap model){
		var createdUser = facade.createUser(new User(0L, user.getName(), user.getEmail()));
		model.addAttribute("createdUser", createdUser);
		return USER_VIEW_NAME;
	}

	/**
	 * Gets a user by id and adds it to model data.
	 *
	 * @param id User id.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping
	public String getUserById(@RequestParam("id") long id, ModelMap model) {
		var user = facade.getUserById(id);
		model.addAttribute("userById", user);
		return USER_VIEW_NAME;
	}

	/**
	 * Gets a user by email and adds it to model data.
	 *
	 * @param email User email.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byEmail")
	public String getUserByEmail(@RequestParam("email") String email, ModelMap model) {
		var user = facade.getUserByEmail(email);
		model.addAttribute("userByEmail", user);
		return USER_VIEW_NAME;
	}

	/**
	 * Gets a list of user by name and adds it to model data. Name is matched using 'contains' approach.
	 *
	 * @param name User name.
	 * @param pageSize Number of ticket entries per page.
	 * @param pageNum Number of page to display.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@GetMapping("/byName")
	public String getUsersByName(@RequestParam("name") String name,
								 @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
								 @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
								 ModelMap model) {
		var users = facade.getUsersByName(name, pageSize, pageNum);
		model.addAttribute("users", users);
		return USER_VIEW_NAME;
	}

	/**
	 * Updates a user by user id and adds the updated object to model data.
	 *
	 * @param user Updated user with the same id.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/update")
	public String updateUser (@ModelAttribute User user, ModelMap model) {
		var updatedUser = facade.updateUser(user);
		model.addAttribute("updatedUser", updatedUser);
		return USER_VIEW_NAME;
	}

	/**
	 * Deletes a user by id. Adds a boolean to model data with information if deletion was successful or not.
	 *
	 * @param id Id of the user to be deleted.
	 * @param model Model data.
	 * @return Name of the view.
	 */
	@PostMapping("/delete")
	public String deleteUser(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.deleteUser(id);
		model.addAttribute("userDeleted", deleteSuccessful);
		return USER_VIEW_NAME;
	}
}
