package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

	private final BookingFacade facade;

	@Autowired
	public UserController(BookingFacade facade) {
		this.facade = facade;
	}

	@GetMapping("/home")
	public String home(){
		return "home";
	}

	@PostMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createUser(@ModelAttribute User user, ModelMap model){
		var createdUser = facade.createUser(new User(0L, user.getName(), user.getEmail()));
		model.addAttribute("createdUser", createdUser);
		return "user";
	}

	@GetMapping("/user")
	public String getUserById(@RequestParam("id") long id, ModelMap model) {
		var user = facade.getUserById(id);
		model.addAttribute("userById", user);
		return "user";
	}

	@GetMapping("/userByEmail")
	public String getUserByEmail(@RequestParam("email") String email, ModelMap model) {
		var user = facade.getUserByEmail(email);
		model.addAttribute("userByEmail", user);
		return "user";
	}

	@GetMapping("/usersByName")
	public String getUsersByName(@RequestParam("name") String name, ModelMap model) {
		var users = facade.getUsersByName(name, 20, 1);
		model.addAttribute("users", users);
		return "users";
	}

	@PostMapping("/updateUser")
	public String updateUser (@ModelAttribute User user, ModelMap model) {
		var updatedUser = facade.updateUser(user);
		model.addAttribute("updatedUser", updatedUser);
		return "user";
	}

	@PostMapping("/deleteUser")
	public String deleteUser(@RequestParam("id") long id, ModelMap model) {
		var deleteSuccessful = facade.deleteUser(id);
		model.addAttribute("userDeleted", deleteSuccessful);
		return "user";
	}

	@ModelAttribute("user")
	public User getEmptyUser() {
		return new User(0L, null, null);
	}

}
