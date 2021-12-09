package org.example.controller;

import org.example.model.Event;
import org.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String home(){
		return "home";
	}

	@ModelAttribute("user")
	public User getEmptyUser() {
		return new User(0L, null, null);
	}

	@ModelAttribute("event")
	public Event getEmptyEvent() {
		return new Event(0L, null, null);
	}
}
