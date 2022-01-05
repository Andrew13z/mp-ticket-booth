package org.example.controller;

import org.example.dto.AccountDto;
import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controller for the home page.
 * @author Andrii Krokhta
 */
@Controller
public class HomeController {

	/**
	 * Sends the user to the home page.
	 *
	 * @return Name of the home view.
	 */
	@GetMapping("/home")
	public String home(){
		return "home";
	}

	/**
	 * Creates a default user instance.
	 *
	 * @return Default user instance.
	 */
	@ModelAttribute("user")
	public UserDto getEmptyUser() {
		var user = new UserDto();
		user.setId(0L);
		return user;
	}

	/**
	 * Creates a default event instance.
	 *
	 * @return Default event instance.
	 */
	@ModelAttribute("event")
	public EventDto getEmptyEvent() {
		return new EventDto();
	}

	/**
	 * Creates a default ticket instance.
	 *
	 * @return Default ticket instance.
	 */
	@ModelAttribute("ticket")
	public TicketDto getEmptyTicket() {
		return new TicketDto();
	}

	/**
	 * Creates a default account instance.
	 *
	 * @return Default account instance.
	 */
	@ModelAttribute("account")
	public AccountDto getEmptyAccount() {
		return new AccountDto();
	}
}
