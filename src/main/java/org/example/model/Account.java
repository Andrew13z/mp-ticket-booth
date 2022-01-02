package org.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigInteger;

@Entity
@Table(name = "account")
public class Account {

	@Id
	private Long id;

	@Column(name = "balance")
	private BigInteger balance;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	public Account() {
	}

	public Account(Long id, BigInteger balance, User user) {
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

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
