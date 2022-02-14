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
import java.util.Objects;

/**
 * Event DTO
 *
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
	@FutureOrPresent(message = "The event date must be today or later")
	private LocalDate date;

	@Min(0)
	private BigDecimal ticketPrice;

	public EventDto() {
		this.ticketPrice = BigDecimal.ZERO;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EventDto)) return false;
		EventDto eventDto = (EventDto) o;
		return Objects.equals(getId(), eventDto.getId()) &&
				Objects.equals(getTitle(), eventDto.getTitle()) &&
				Objects.equals(getDate(), eventDto.getDate()) &&
				Objects.equals(getTicketPrice(), eventDto.getTicketPrice());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getTitle(), getDate(), getTicketPrice());
	}
}
