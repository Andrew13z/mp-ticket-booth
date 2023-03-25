package org.example.dto;

import org.example.enums.Category;
import org.example.validation.group.OnTicketCreate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Ticket DTO
 * @author Andrii Krokhta
 */
public class TicketDto {

	@Null(groups = OnTicketCreate.class)
	private String id;

	@Valid
	@NotNull(groups = OnTicketCreate.class)
	private UserDto user;

	@Valid
	@NotNull(groups = OnTicketCreate.class)
	private EventDto event;

	@NotNull(groups = OnTicketCreate.class)
	private Category category;

	private int place;

	public TicketDto() {
	}

	public TicketDto(String id, UserDto user, EventDto event, Category category, int place) {
		this.id = id;
		this.user = user;
		this.event = event;
		this.category = category;
		this.place = place;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	@Override
	public String toString() {
		return "TicketDto{" +
				"id=" + id +
				", user name=" + user.getName() +
				", event title=" + event.getTitle() +
				", category=" + category +
				", place=" + place +
				'}';
	}
}
