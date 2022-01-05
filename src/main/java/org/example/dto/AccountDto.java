package org.example.dto;

import java.math.BigDecimal;

/**
 * Account DTO
 * @author Andrii Krokhta
 */
public class AccountDto {

	private Long id;

	private BigDecimal balance;

	public AccountDto() {
	}

	public AccountDto(Long id, BigDecimal balance) {
		this.id = id;
		this.balance = balance;
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
		this.balance = balance;
	}

}
