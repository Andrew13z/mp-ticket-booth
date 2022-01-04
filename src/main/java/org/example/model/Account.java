package org.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
public class Account {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	public Account() {
	}

	public Account(Long id, BigDecimal balance) {
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
