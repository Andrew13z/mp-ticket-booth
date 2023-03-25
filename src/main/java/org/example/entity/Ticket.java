package org.example.entity;

import org.example.enums.Category;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Ticket entity
 * @author Andrii Krokhta
 */
@Entity
@Table(name = "TICKETS")
public class Ticket {

	@Id
	private String id;

	private String userId;

	private String eventId;

//	@Enumerated(EnumType.STRING)
	private Category category;

	private int place;

	public Ticket() {
	}

	public Ticket(String id, String userId, String eventId, Category category, int place) {
		this.id = id;
		this.userId = userId;
		this.eventId = eventId;
		this.category = category;
		this.place = place;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUser(String userId) {
		this.userId = userId;
	}

	public void setEvent(String eventId) {
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
}
