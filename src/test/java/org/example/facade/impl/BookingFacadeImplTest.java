package org.example.facade.impl;

import org.example.dto.EventDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Sql(value = {"classpath:drop-tables.sql"})
@Sql(value = {"classpath:create-tables.sql"})
@SpringBootTest
class BookingFacadeImplTest {

	private final String userName = "Name";

	@Autowired
	private BookingFacadeImpl facade;

	@Test
	void endToEndTest(){
		//Creating user
		String userName = "Name";
		var user = new UserDto(0L, userName, "email@mail.com");
		var savedUser = facade.createUser(user);
		var savedUserId = savedUser.getId();

		assertEquals(userName, savedUser.getName());

		//Creating event
		String eventTitle = "Title";
		var event = new EventDto(0L, eventTitle, LocalDate.now(), BigDecimal.ZERO);
		var savedEvent = facade.createEvent(event);
		var savedEventId = savedEvent.getId();

		assertEquals(eventTitle, savedEvent.getTitle());

		//Creating ticket
		var ticket = facade.bookTicket(savedUserId, savedEventId, Category.STANDARD, 0);

		assertEquals(savedUserId, ticket.getUser().getId());
		assertEquals(savedEventId, ticket.getEvent().getId());

		//Checking ticket by user
		var userTickets = facade.getBookedTicketsByUserId(savedUserId, 10, 0);

		assertEquals(1, userTickets.size());
		assertEquals(savedUserId, userTickets.get(0).getUser().getId());

		//Checking ticket by event
		var eventTickets = facade.getBookedTicketsByEventId(savedEventId, 10, 0);

		assertEquals(1, eventTickets.size());
		assertEquals(savedEventId, eventTickets.get(0).getEvent().getId());

		//Canceling ticket
		facade.cancelTicket(ticket.getId());
		//Checking that ticket doesn't exist
		assertEquals(0, facade.getBookedTicketsByEventId(savedEventId, 10, 0).size());
	}
}
