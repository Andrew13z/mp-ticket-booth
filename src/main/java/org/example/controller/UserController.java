package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
		var savedUser = facade.createUser(new User(0L, user.getName(), user.getEmail()));
		model.addAttribute("user", savedUser);
		return "success";
	}

	@GetMapping("/user/{id}")
	public String getUserById(@PathVariable("id") long id) {
		var user = facade.getUserById(id);
		return null;
	}

	@ModelAttribute("user")
	public User getEmptyUser() {
		return new User(0L, null, null);
	}

}
