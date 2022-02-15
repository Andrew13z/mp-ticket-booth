package org.example.service.impl;

import org.example.exception.AccountBalanceException;
import org.example.exception.EntityNotFoundException;
import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.example.util.TestUtils.DEFAULT_ACCOUNT_BALANCE;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.createDefaultAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@Mock
	private AccountRepository mockRepository;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Test
	void refillAccountTestWithExistingAccountId() {
		when(mockRepository.findById(ID_ONE)).thenReturn(Optional.of(createDefaultAccount()));
		when(mockRepository.save(any(Account.class))).then(returnsFirstArg());

		var account = accountService.refillAccount(ID_ONE, BigDecimal.TEN);
		assertEquals(DEFAULT_ACCOUNT_BALANCE.add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP), account.getBalance());
	}

	@Test
	void refillAccountTestWithNotExistingAccountId() {
		when(mockRepository.findById(ID_ONE)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> accountService.refillAccount(ID_ONE, BigDecimal.TEN));
	}

	@Test
	void chargeForTicketTestWithEnoughBalance() {
		when(mockRepository.findById(ID_ONE)).thenReturn(Optional.of(createDefaultAccount()));
		when(mockRepository.save(any(Account.class))).then(returnsFirstArg());

		var account = accountService.chargeForTicket(ID_ONE, BigDecimal.TEN);
		assertEquals(DEFAULT_ACCOUNT_BALANCE.subtract(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP), account.getBalance());
	}

	@Test
	void chargeForTicketTestWithNotEnoughBalance() {
		when(mockRepository.findById(ID_ONE)).thenReturn(Optional.of(createDefaultAccount()));
		var ticketPrice = DEFAULT_ACCOUNT_BALANCE.add(BigDecimal.ONE);
		assertThrows(AccountBalanceException.class, () -> accountService.chargeForTicket(ID_ONE, ticketPrice));
	}

	@Test
	void chargeForTicketTestWithNotExistingAccountId() {
		when(mockRepository.findById(ID_ONE)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> accountService.chargeForTicket(ID_ONE, BigDecimal.TEN));
	}
}
