package org.example.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Event entity
 */
@Document("events")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event {

	@Id
	private String id;

	@Indexed(unique = true)
	private String title;

	@Indexed(name = "DATE_HELD")
	private LocalDate date;

	@Indexed(name = "TICKET_PRICE")
	private BigDecimal ticketPrice;

	public Event() {
		this.ticketPrice = BigDecimal.ZERO;
	}

	public Event(String id, String title, LocalDate date, BigDecimal ticketPrice) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.ticketPrice = ticketPrice != null ? ticketPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigDecimal getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(BigDecimal ticketPrice) {
		this.ticketPrice = ticketPrice != null ? ticketPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}
}
