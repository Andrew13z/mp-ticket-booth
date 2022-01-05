package org.example.service;

import org.example.dto.AccountDto;

import java.math.BigDecimal;

public interface AccountService {

	/**
	 * Creates and account for user id.
	 *
	 * @param userId User id.
	 * @return created account.
	 */
	AccountDto createAccount(Long userId);

	/**
	 * Adds the provided refill sum to the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param refillSum Sum to be added to the account.
	 * @return Flag whether anything has been canceled.
	 */
	AccountDto refillAccount(Long accountId, BigDecimal refillSum);

	/**
	 * Subtracts the provided ticket price from the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param ticketPrice Amount to be subtracted from the account.
	 * @return updated account.
	 */
	AccountDto chargeForTicket(Long accountId, BigDecimal ticketPrice);

	/**
	 * Deletes account by its id.
	 *
	 * @param accountId Account id.
	 */
	void deleteById(Long accountId);
}
