package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Event DTO
 * @author Andrii Krokhta
 */
public class EventDto {

	private Long id;//todo add validation to check that id is null during event creation

	private String title;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	private BigDecimal ticketPrice;

	public EventDto() {
	}

	public EventDto(Long id, String title, LocalDate date, BigDecimal ticketPrice) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.ticketPrice = ticketPrice.setScale(2, RoundingMode.HALF_UP);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		this.ticketPrice = ticketPrice;
	}
}
