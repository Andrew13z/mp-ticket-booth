package org.example.model;

/**
 * Ticket entity
 * @author Andrii Krokhta
 */
public class Ticket {
	public enum Category {STANDARD, PREMIUM, BAR}

	private long id;

	private long userId;

	private long eventId;

	private Category category;

	private int place;

	public Ticket() {
	}

	public Ticket(long id, long userId, long eventId, Category category, int place) {
		this.id = id;
		this.userId = userId;
		this.eventId = eventId;
		this.category = category;
		this.place = place;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEventId() {
		return eventId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
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
		return "Ticket{" +
				"id=" + id +
				", userId=" + userId +
				", eventId=" + eventId +
				", category=" + category +
				", place=" + place +
				'}';
	}
}
