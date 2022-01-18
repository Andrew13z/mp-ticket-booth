package org.example.controller;

import org.example.dto.AccountDto;
import org.example.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

	private final BookingFacade facade;

	@Autowired
	public AccountController(BookingFacade facade) {
		this.facade = facade;
	}

	@PatchMapping("/userId")
	public AccountDto refillAccount(@PathVariable("userId") Long userId,
									@RequestBody BigDecimal refillSum) {
		return facade.refillAccount(userId, refillSum);
	}
}
