package org.example.service;

import org.example.model.Account;

import java.math.BigInteger;

public interface AccountService {

	/**
	 * Adds the provided refill sum to the specified account by id.
	 *
	 * @param accountId Account id.
	 * @param refillSum Sum to be added to the account.
	 * @return Flag whether anything has been canceled.
	 */
	Account refillAccount(Long accountId, BigInteger refillSum);
}
