package org.example.model;

/**
 * Bulder for ticket entity.
 */
public class TicketBuilder {
	private Long id;
	private Long userId;
	private Long eventId;
	private Ticket.Category category;
	private int place;

	public TicketBuilder setId(Long id) {
		this.id = id;
		return this;
	}

	public TicketBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public TicketBuilder setEventId(Long eventId) {
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
		var ticket = new Ticket();
		ticket.setId(id);
		setUserIfNotNull(ticket);
		setEventIfNotNull(ticket);
		ticket.setCategory(category);
		ticket.setPlace(place);

		return ticket;
	}

	private void setEventIfNotNull(Ticket ticket) {
		if (eventId != null) {
			var event = new Event();
			event.setId(eventId);
			ticket.setEvent(event);
		}
	}

	private void setUserIfNotNull(Ticket ticket) {
		if (userId != null) {
			var user = new User();
			user.setId(userId);
			ticket.setUser(user);
		}
	}
}