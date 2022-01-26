package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.validation.group.OnCreate;
import org.example.validation.group.OnTicketCreate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Event DTO
 * @author Andrii Krokhta
 */
public class EventDto {

	@Null(groups = OnCreate.class)
	@NotNull(groups = OnTicketCreate.class)
	private Long id;

	@NotBlank(groups = OnCreate.class)
	private String title;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(groups = OnCreate.class)
	@FutureOrPresent
	private LocalDate date;

	@Min(0)
	private BigDecimal ticketPrice;

	public EventDto() {
	}

	public EventDto(Long id, String title, LocalDate date, BigDecimal ticketPrice) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.ticketPrice = ticketPrice != null ? ticketPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
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
		this.ticketPrice = ticketPrice != null ? ticketPrice.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
	}
}
