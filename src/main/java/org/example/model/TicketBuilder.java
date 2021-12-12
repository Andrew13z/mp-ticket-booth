package org.example.model;

/**
 * Bulder for ticket entity.
 */
public class TicketBuilder {
	private long id;
	private long userId;
	private long eventId;
	private Ticket.Category category;
	private int place;

	public TicketBuilder setId(long id) {
		this.id = id;
		return this;
	}

	public TicketBuilder setUserId(long userId) {
		this.userId = userId;
		return this;
	}

	public TicketBuilder setEventId(long eventId) {
		this.eventId = eventId;
		return this;
	}

	public TicketBuilder setCategory(Ticket.Category category) {
		this.category = category;
		return this;
	}

	public TicketBuilder setPlace(int place) {
		this.place = place;
		return this;
	}

	public Ticket createTicket() {
		return new Ticket(id, userId, eventId, category, place);
	}
}