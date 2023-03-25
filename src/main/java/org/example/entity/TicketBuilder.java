package org.example.entity;

import org.example.enums.Category;

/**
 * Builder for ticket entity.
 */
public class TicketBuilder {
	private String id;
	private String userId;
	private String eventId;
	private Category category;
	private int place;

	public TicketBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public TicketBuilder setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public TicketBuilder setEventId(String eventId) {
		this.eventId = eventId;
		return this;
	}

	public TicketBuilder setCategory(Category category) {
		this.category = category;
		return this;
	}

	public TicketBuilder setPlace(int place) {
		this.place = place;
		return this;
	}

	public Ticket createTicket() {
		var ticket = new Ticket();
		ticket.setId(id);
		ticket.setUser(userId);
		ticket.setEvent(eventId);
		ticket.setCategory(category);
		ticket.setPlace(place);

		return ticket;
	}
}