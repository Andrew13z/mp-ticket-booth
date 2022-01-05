package org.example.dto;

import org.example.enums.Category;

/**
 * Ticket DTO
 * @author Andrii Krokhta
 */
public class TicketDto {

	private Long id;

	private UserDto user;

	private EventDto event;

	private Category category;

	private int place;

	public TicketDto() {
	}

	public TicketDto(Long id, UserDto user, EventDto event, Category category, int place) {
		this.id = id;
		this.user = user;
		this.event = event;
		this.category = category;
		this.place = place;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventDto getEvent() {
		return event;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public void setEvent(EventDto event) {
		this.event = event;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}
}
