package org.example.service.impl;

import org.example.dto.AccountDto;
import org.example.exception.AccountBalanceException;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.service.AccountService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

	private final AccountRepository repository;
	private final ModelMapper mapper;

	@Autowired
	public AccountServiceImpl(AccountRepository repository, ModelMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDto createAccount(Long userId) {
		var account = new Account();
		account.setId(userId);
		return mapper.map(repository.save(account), AccountDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDto refillAccount(Long accountId, BigDecimal refillSum) {
		var account = repository.findById(accountId)
				.orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + accountId));
		var accountBalance = account.getBalance();
		account.setBalance(accountBalance.add(refillSum));
		logger.info("Refilled account (id: {}) by {}.", accountId, refillSum);
		return mapper.map(repository.save(account), AccountDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDto chargeForTicket(Long accountId, BigDecimal ticketPrice) {
		var account = repository.findById(accountId)
				.orElseThrow(() -> new EntityNotFoundException("Account not found by id: " + accountId));

		var accountBalance = account.getBalance();
		if (accountBalance.compareTo(ticketPrice) < 0) {
			logger.warn("Account {} has insufficient funds to buy the ticket with price {}.", accountId, ticketPrice);
			throw new AccountBalanceException("Account has insufficient funds.");
		}
		account.setBalance(accountBalance.subtract(ticketPrice));
		logger.info("Charged account (id: {}) for {}.", accountId, ticketPrice);
		return mapper.map(repository.save(account), AccountDto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteById(Long accountId) {
		repository.deleteById(accountId);
	}
}
