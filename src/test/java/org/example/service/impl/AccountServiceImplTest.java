package org.example.service.impl;

import org.example.exception.AccountBalanceException;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	private final Long ID = 1L;
	private final BigDecimal BALANCE = BigDecimal.valueOf(100);

	@Mock
	private AccountRepository mockRepository;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private AccountServiceImpl accountService;

	@Test
	void refillAccountTestWithExistingAccountId() {
		when(mockRepository.findById(ID)).thenReturn(Optional.of(new Account(ID, BALANCE)));
		when(mockRepository.save(any(Account.class))).then(returnsFirstArg());

		var account = accountService.refillAccount(ID, BigDecimal.TEN);
		assertEquals(BALANCE.add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP), account.getBalance());
	}

	@Test
	void refillAccountTestWithNotExistingAccountId() {
		when(mockRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> accountService.refillAccount(ID, BigDecimal.TEN));
	}

	@Test
	void chargeForTicketTestWithEnoughBalance() {
		when(mockRepository.findById(ID)).thenReturn(Optional.of(new Account(ID, BALANCE)));
		when(mockRepository.save(any(Account.class))).then(returnsFirstArg());

		var account = accountService.chargeForTicket(ID, BigDecimal.TEN);
		assertEquals(BALANCE.subtract(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP), account.getBalance());
	}

	@Test
	void chargeForTicketTestWithNotEnoughBalance() {
		when(mockRepository.findById(ID)).thenReturn(Optional.of(new Account(ID, BALANCE)));
		var ticketPrice = BALANCE.add(BigDecimal.ONE);
		assertThrows(AccountBalanceException.class, () -> accountService.chargeForTicket(ID, ticketPrice));
	}

	@Test
	void chargeForTicketTestWithNotExistingAccountId() {
		when(mockRepository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> accountService.chargeForTicket(ID, BigDecimal.TEN));
	}
}
