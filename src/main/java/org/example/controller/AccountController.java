package org.example.controller;

import org.example.dto.AccountDto;
import org.example.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/accounts")
@Validated
public class AccountController {

	private final BookingFacade facade;

	@Autowired
	public AccountController(BookingFacade facade) {
		this.facade = facade;
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<AccountDto> refillAccount(@PathVariable("id") Long id,
												   @RequestBody @Min(0) BigDecimal refillSum) {
		return ResponseEntity.ok(facade.refillAccount(id, refillSum));
	}
}
