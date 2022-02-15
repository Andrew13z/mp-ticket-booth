package org.example.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "ACCOUNTS")
public class Account {

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id", referencedColumnName = "id", updatable = false, insertable = false)
	private User user;

	public Account() {
		this.balance = BigDecimal.ZERO;
	}

	public Account(Long id, BigDecimal balance, User user) {
		this.id = id;
		this.balance = balance;
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
