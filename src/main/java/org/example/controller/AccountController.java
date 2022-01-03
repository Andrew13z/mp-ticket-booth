package org.example.controller;

import org.example.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/account")
public class AccountController {

	private final BookingFacade facade;

	@Autowired
	public AccountController(BookingFacade facade) {
		this.facade = facade;
	}

	@PostMapping
	public String refillAccount(@RequestParam("userId") Long userId,
								@RequestParam("refillSum") BigDecimal refillSum,
								ModelMap model) {
		var account = facade.refillAccount(userId, refillSum);
		model.addAttribute("account", account);
		return "account";
	}
}
