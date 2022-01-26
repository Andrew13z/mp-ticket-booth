package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.example.util.TestUtils.DEFAULT_EVENT_DATE;
import static org.example.util.TestUtils.DEFAULT_EVENT_TITLE;
import static org.example.util.TestUtils.DEFAULT_TICKET_PLACE;
import static org.example.util.TestUtils.DEFAULT_TICKET_PRICE;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.createEventDtoWithoutId;
import static org.example.util.TestUtils.createTicketDtoFotTicketCreateOperation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TicketControllerTest {

	private static final String CONTROLLER_PATH = "/tickets";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testCreateTicket_WithValidData() throws Exception {

		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createTicketDtoFotTicketCreateOperation())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.user.id").value(ID_ONE))
				.andExpect(jsonPath("$.event.id").value(ID_ONE))
				.andExpect(jsonPath("$.category").value(Category.STANDARD.toString()))
				.andExpect(jsonPath("$.place").value(DEFAULT_TICKET_PLACE));
	}

	/*@Test
	void testGetTicketsByUser_WithExistingUser() throws Exception {
		var result = mockMvc.perform(get("/ticket/byUser")
						.param("userId", String.valueOf(USER_ID))
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByUser"))
				.andReturn();
		var tickets = (List<TicketDto>) result.getModelAndView().getModel().get("ticketsByUser");
		assertEquals(USER_ID, tickets.get(0).getUser().getId());
	}

	@Test
	void testGetTicketsByUser_WithNotExistingUser() throws Exception {
		var result = mockMvc.perform(get("/ticket/byUser")
						.param("userId", "100")
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByUser"))
				.andReturn();
		var tickets = (List<TicketDto>) result.getModelAndView().getModel().get("ticketsByUser");
		assertEquals(0, tickets.size());
	}

	@Test
	void testGetTicketsByUserPdf() throws Exception {
		var result = mockMvc.perform(get("/ticket/byUser")
						.header("Accept", "application/pdf")
						.param("userId", "1")
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE));
	}

	@Test
	void testGetTicketsByEvent_WithExistingEvent() throws Exception {
		var result = mockMvc.perform(get("/ticket/byEvent")
						.param("eventId", String.valueOf(EVENT_ID))
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByEvent"))
				.andReturn();
		var tickets = (List<TicketDto>) result.getModelAndView().getModel().get("ticketsByEvent");
		assertEquals(EVENT_ID, tickets.get(0).getEvent().getId());
	}

	@Test
	void testGetTicketsByEvent_WithNotExistingEvent() throws Exception {
		var result = mockMvc.perform(get("/ticket/byEvent")
						.param("eventId", "100")
						.param("pageSize", "1")
						.param("pageNum", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ticketsByEvent"))
				.andReturn();
		var tickets = (List<TicketDto>) result.getModelAndView().getModel().get("ticketsByEvent");
		assertEquals(0, tickets.size());
	}

	@Test
	void deleteTicketTest_WithExistingId() throws Exception {
		var result = mockMvc.perform(post("/ticket/delete")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("deleteTicketId"))
				.andReturn();

		var deleteTicketId = (Long) result.getModelAndView().getModel().get("deleteTicketId");
		assertEquals(TICKET_ID, deleteTicketId);
	}

	@Test
	void deleteTicketTest_WithNotExistingId() throws Exception {
		mockMvc.perform(post("/ticket/delete")
						.param("id", "100"))
				.andExpect(status().isInternalServerError());
	}*/
}
