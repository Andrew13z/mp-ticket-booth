package org.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
public class Account {

	@Id
	@SequenceGenerator(name = "ACCOUNTS_ID_SEQ", sequenceName = "ACCOUNTS_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_ID_SEQ")
	@Column(name = "ID")
	private Long id;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(name = "ID", insertable = false, updatable = false)
	private User user;

	public Account() {
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
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
