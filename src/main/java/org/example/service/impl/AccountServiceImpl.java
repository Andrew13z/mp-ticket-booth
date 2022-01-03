package org.example.service.impl;

import org.example.exception.AccountBalanceException;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.model.User;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private final AccountRepository repository;

	@Autowired
	public AccountServiceImpl(AccountRepository repository) {
		this.repository = repository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account createAccount(Long userId) {
		var account = new Account();
		account.setUser(new User(userId, null, null));
		return repository.save(account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account refillAccount(Long accountId, BigDecimal refillSum) {
		var account = repository.findById(accountId)
				.orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + accountId));
		var accountBalance = account.getBalance();
		account.setBalance(accountBalance.add(refillSum));
		return repository.save(account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account chargeForTicket(Long accountId, BigDecimal ticketPrice) {
		var account = repository.findById(accountId)
				.orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + accountId));

		var accountBalance = account.getBalance();
		if (accountBalance.compareTo(ticketPrice) < 0) {
			logger.warn("Account {} has insufficient funds to buy the ticket with price {}.", accountId, ticketPrice);
			throw new AccountBalanceException("Account has insufficient funds.");
		}
		account.setBalance(accountBalance.subtract(ticketPrice));
		return repository.save(account);
	}

	@Override
	public void deleteById(Long accountId) {
		repository.deleteById(accountId);
	}
}
