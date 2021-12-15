package org.example.facade.impl;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingFacadeImplTest {

	private final String userName = "Name";

	@Autowired
	private BookingFacadeImpl facade;

	@Test
	void dataInitializedTest(){
		assertNotNull(facade.getUserById(10));
		var event = facade.getEventById(4);
		assertNotNull(event);
		assertNotNull(facade.getBookedTickets(event, 2, 1));
	}

	@Test
	void endToEndTest(){
		//Creating user
		String userName = "Name";
		User user = new User(0, userName, "email@mail.com");
		var savedUser = facade.createUser(user);

		assertEquals(userName, savedUser.getName());

		//Creating event
		String eventTitle = "Title";
		Event event = new Event(0, eventTitle, LocalDate.now());
		var savedEvent = facade.createEvent(event);

		assertEquals(eventTitle, savedEvent.getTitle());

		//Creating ticket
		var ticket = facade.bookTicket(savedUser.getId(), savedEvent.getId(), Ticket.Category.STANDARD, 1);

		assertEquals(savedUser.getId(), ticket.getUserId());
		assertEquals(savedEvent.getId(), ticket.getEventId());

		//Checking ticket by user
		var userTickets = facade.getBookedTickets(savedUser, 10, 1);

		assertEquals(1, userTickets.size());
		assertEquals(savedUser.getId(), userTickets.get(0).getUserId());

		//Checking ticket by event
		var eventTickets = facade.getBookedTickets(savedEvent, 10, 1);

		assertEquals(1, eventTickets.size());
		assertEquals(savedEvent.getId(), eventTickets.get(0).getEventId());

		//Canceling ticket
		assertTrue(facade.cancelTicket(ticket.getId()));
	}
}
