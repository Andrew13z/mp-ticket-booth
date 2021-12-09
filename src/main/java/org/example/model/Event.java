package org.example.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Event {

	private long id;

	private String title;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;

	public Event(long id, String title, LocalDate date) {
		this.id = id;
		this.title = title;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Event{" +
				"id=" + id +
				", title='" + title + '\'' +
				", date=" + date +
				'}';
	}
}
