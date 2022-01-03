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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Ticket entity
 * @author Andrii Krokhta
 */
@Entity
@Table(name = "TICKETS")
public class Ticket {

	public enum Category {STANDARD, PREMIUM, BAR}

	@Id
	@SequenceGenerator(name = "TICKETS_ID_SEQ", sequenceName = "TICKETS_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKETS_ID_SEQ")
	@Column(name = "ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@ManyToOne
	@JoinColumn(name = "EVENT_ID")
	private Event event;

	@Column(name = "CATEGORY")
	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(name = "PLACE")
	private int place;

	public Ticket() {
	}

	public Ticket(Long id, User user, Event event, Category category, int place) {
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
