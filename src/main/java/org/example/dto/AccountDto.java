package org.example.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Account DTO
 * @author Andrii Krokhta
 */
public class AccountDto {

	private Long id;

	private BigDecimal balance;

	public AccountDto() {
		this.balance = BigDecimal.ZERO;
	}

	public AccountDto(Long id, BigDecimal balance) {
		this.id = id;
		this.balance = balance != null ? balance.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance != null ? balance.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}

}
