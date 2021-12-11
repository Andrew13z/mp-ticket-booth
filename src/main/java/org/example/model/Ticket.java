package org.example.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ticket")
public class Ticket {
	public enum Category {STANDARD, PREMIUM, BAR}

	@XStreamAlias("id")
	private long id;

	@XStreamAlias("userId")
	private long userId;

	@XStreamAlias("eventId")
	private long eventId;

	@XStreamAlias("category")
	private Category category;

	@XStreamAlias("place")
	private int place;

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
