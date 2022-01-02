package org.example.service.impl;

import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository repository;

	@Autowired
	public AccountServiceImpl(AccountRepository repository) {
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account refillAccount(Long accountId, BigInteger refillSum) {
		var account = repository.findById(accountId)
				.orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + accountId));
		account.getBalance().add(refillSum);
		return account;
	}
}
