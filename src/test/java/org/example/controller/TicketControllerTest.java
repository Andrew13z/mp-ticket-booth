package org.example.controller;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.repository.InMemoryStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TicketControllerTest {

	private static final long TICKET_ID = 1L;
	private static final long USER_ID = 6L;
	private static final User USER = new User(USER_ID, null, null);
	private static final long EVENT_ID = 1L;
	private static final Event EVENT = new Event(EVENT_ID, null, null);
	private static final int PLACE = 130;

	private MockMvc mockMvc;

	@Autowired
	private InMemoryStorage<Ticket> storage;

	@BeforeEach
	void setUp(WebApplicationContext wac){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@AfterEach
	void cleanUp() {
		storage.getData().remove(1L);
		storage.getData().put(1L, new Ticket (TICKET_ID, USER, EVENT, Ticket.Category.STANDARD, PLACE));
	}

	@Test
	void testCreateEvent() throws Exception{
		var localDate = LocalDate.now();
		var result = mockMvc.perform(post("/ticket")
						.flashAttr("ticket", new Ticket(0L, USER, EVENT, Ticket.Category.BAR, PLACE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("createdTicket"))
				.andReturn();
		var ticket = (Ticket) result.getModelAndView().getModel().get("createdTicket");

		assertEquals(USER_ID, ticket.getUser().getId());
		assertEquals(EVENT_ID, ticket.getEvent().getId());
		assertEquals(Ticket.Category.BAR, ticket.getCategory());
		assertEquals(PLACE, ticket.getPlace());
	}

	@Test
	void testGetTicketsByUser_WithExistingUser() throws Exception{
		var result = mockMvc.perform(get("/ticket/byUser")
						.param("userId", String.valueOf(USER_ID))
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByUser"))
				.andReturn();
		var tickets = (List<Ticket>) result.getModelAndView().getModel().get("ticketsByUser");
		assertEquals(USER_ID, tickets.get(0).getUser().getId());
	}

	@Test
	void testGetTicketsByUser_WithNotExistingUser() throws Exception{
		var result = mockMvc.perform(get("/ticket/byUser")
						.param("userId", "100")
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByUser"))
				.andReturn();
		var tickets = (List<Ticket>) result.getModelAndView().getModel().get("ticketsByUser");
		assertEquals(0, tickets.size());
	}

	@Test
	void testGetTicketsByUserPdf() throws Exception{
		var result = mockMvc.perform(get("/ticket/byUser")
						.header("Accept", "application/pdf")
						.param("userId", "1")
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE));
	}

	@Test
	void testGetTicketsByEvent_WithExistingEvent() throws Exception{
		var result = mockMvc.perform(get("/ticket/byEvent")
						.param("eventId", String.valueOf(EVENT_ID))
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByEvent"))
				.andReturn();
		var tickets = (List<Ticket>) result.getModelAndView().getModel().get("ticketsByEvent");
		assertEquals(EVENT_ID, tickets.get(0).getEvent().getId());
	}

	@Test
	void testGetTicketsByEvent_WithNotExistingEvent() throws Exception{
		var result = mockMvc.perform(get("/ticket/byEvent")
						.param("eventId", "100")
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByEvent"))
				.andReturn();
		var tickets = (List<Ticket>) result.getModelAndView().getModel().get("ticketsByEvent");
		assertEquals(0, tickets.size());
	}

	@Test
	void deleteTicketTest_WithExistingId() throws Exception{
		var result = mockMvc.perform(post("/ticket/delete")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketDeleted"))
				.andReturn();

		var ticketDeleted = (boolean) result.getModelAndView().getModel().get("ticketDeleted");
		assertTrue(ticketDeleted);
	}

	@Test
	void deleteTicketTest_WithNotExistingId() throws Exception{
		var result = mockMvc.perform(post("/ticket/delete")
						.param("id", "100"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketDeleted"))
				.andReturn();

		var ticketDeleted = (boolean) result.getModelAndView().getModel().get("ticketDeleted");
		assertFalse(ticketDeleted);
	}
}
