package org.example.dto;

import org.example.enums.Category;

/**
 * Builder for ticket dto.
 */
public class TicketDtoBuilder {
	private Long id;
	private Long userId;
	private Long eventId;
	private Category category;
	private int place;

	public TicketDtoBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public TicketDtoBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public TicketDtoBuilder setEventId(Long eventId) {
		this.eventId = eventId;
		return this;
	}

	public TicketDtoBuilder setCategory(Category category) {
		this.category = category;
		return this;
	}

	public TicketDtoBuilder setPlace(int place) {
		this.place = place;
		return this;
	}

	public TicketDto createTicket() {
		var ticket = new TicketDto();
		ticket.setId(id);
		setUserIfNotNull(ticket);
		setEventIfNotNull(ticket);
		ticket.setCategory(category);
		ticket.setPlace(place);

		return ticket;
	}

	private void setEventIfNotNull(TicketDto ticket) {
		if (eventId != null) {
			var event = new EventDto();
			event.setId(eventId);
			ticket.setEvent(event);
		}
	}

	private void setUserIfNotNull(TicketDto ticket) {
		if (userId != null) {
			var user = new UserDto();
			user.setId(userId);
			ticket.setUser(user);
		}
	}
}