package org.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Ticket entity
 * @author Andrii Krokhta
 */
@Entity
@Table(name = "ticket")
public class Ticket {

	public enum Category {STANDARD, PREMIUM, BAR}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_sequence")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user")
	private User user;

	@ManyToOne
	@JoinColumn(name = "event")
	private Event event;

	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(name = "place")
	private int place;

	public Ticket() {
	}

	public Ticket(long id, User user, Event event, Category category, int place) {
		this.id = id;
		this.user = user;
		this.event = event;
		this.category = category;
		this.place = place;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setEvent(Event event) {
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
