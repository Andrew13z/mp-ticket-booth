package org.example.controller;

import org.example.dto.AccountDto;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/accounts")
@Validated
public class AccountController {

	private final AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PatchMapping(value = "/{id}")
	public ResponseEntity<AccountDto> refillAccount(@PathVariable("id") Long id,
													@RequestBody @Min(0) BigDecimal refillSum) {
		return ResponseEntity.ok(accountService.refillAccount(id, refillSum));
	}
}
