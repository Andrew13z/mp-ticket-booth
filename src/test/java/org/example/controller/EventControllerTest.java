package org.example.controller;

import org.example.dto.EventDto;
import org.example.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = { "classpath:drop-tables.sql" })
@Sql(value = { "classpath:init-event.sql" })
@SpringBootTest
class EventControllerTest {

	private static final String TITLE = "Matrix Lucky Hand";
	private static final LocalDate DATE = LocalDate.of(2021, 12, 15);
	private static final BigDecimal PRICE = BigDecimal.ZERO;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext wac){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Sql(value = { "classpath:drop-tables.sql" })
	@Sql({ "classpath:create-tables.sql" })
	@Test
	void testCreateEvent() throws Exception{
		var localDate = LocalDate.now();
		var result = mockMvc.perform(post("/event")
						.flashAttr("event", new EventDto(0L, "New Title", localDate, PRICE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("createdEvent"))
				.andReturn();
		var event = (EventDto) result.getModelAndView().getModel().get("createdEvent");
		assertEquals("New Title", event.getTitle());
		assertEquals(localDate, event.getDate());
		assertEquals(0, PRICE.compareTo(event.getTicketPrice()));
	}

	@Test
	void testGetEventById_WithExistingId() throws Exception{
		var result = mockMvc.perform(get("/event")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventById"))
				.andReturn();
		var event = (EventDto) result.getModelAndView().getModel().get("eventById");
		assertEquals(TITLE, event.getTitle());
	}

	@Test
	void testGetEventById_WithNotExistingId() throws Exception{
		mockMvc.perform(get("/event")
						.param("id", "100"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetEventsByTitle_WithExistingTitle() throws Exception{
		var result = mockMvc.perform(get("/event/byTitle")
						.param("title", TITLE)
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventsByTitle"))
				.andReturn();
		var events = (List<EventDto>) result.getModelAndView().getModel().get("eventsByTitle");
		assertEquals(TITLE, events.get(0).getTitle());
	}

	@Test
	void testGetEventsByTitle_WithNotExistingTitle() throws Exception{
		var result = mockMvc.perform(get("/event/byTitle")
						.param("title", "Not a title")
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventsByTitle"))
				.andReturn();
		var events = (List<EventDto>) result.getModelAndView().getModel().get("eventsByTitle");
		assertEquals(0, events.size());
	}

	@Test
	void testGetEventsByDate_WithExistingDate() throws Exception{
		var result = mockMvc.perform(get("/event/byDate")
						.param("date", DATE.toString())
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventsByDate"))
				.andReturn();
		var events = (List<EventDto>) result.getModelAndView().getModel().get("eventsByDate");
		assertEquals(DATE, events.get(0).getDate());
	}

	@Test
	void testGetEventsByDate_WithNotExistingDate() throws Exception{
		var result = mockMvc.perform(get("/event/byDate")
						.param("date", LocalDate.of(1000, 1, 1).toString())
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("eventsByDate"))
				.andReturn();
		var events = (List<EventDto>) result.getModelAndView().getModel().get("eventsByDate");
		assertEquals(0, events.size());
	}

	@Test
	void updateEventTest_WithAllAttributesUpdated() throws Exception{
		var localDate = LocalDate.now();
		var result = mockMvc.perform(post("/event/update")
						.flashAttr("event", new EventDto(1L, "New Title", localDate, PRICE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedEvent"))
				.andReturn();
		var event = (EventDto) result.getModelAndView().getModel().get("updatedEvent");

		assertEquals(1L, event.getId());
		assertEquals("New Title", event.getTitle());
		assertEquals(localDate, event.getDate());
	}

	@Test
	void updateEventTest_WithoutUpdatedTitle() throws Exception{
		var localDate = LocalDate.now();
		var result = mockMvc.perform(post("/event/update")
						.flashAttr("event", new EventDto(1L, "", localDate, PRICE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedEvent"))
				.andReturn();
		var event = (EventDto) result.getModelAndView().getModel().get("updatedEvent");

		assertEquals(1L, event.getId());
		assertEquals(TITLE, event.getTitle());
		assertEquals(localDate, event.getDate());
	}

	@Test
	void updateEventTest_WithoutUpdatedDate() throws Exception{
		var localDate = LocalDate.now();
		var result = mockMvc.perform(post("/event/update")
						.flashAttr("event", new EventDto(1L, "New Title", null, PRICE)))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedEvent"))
				.andReturn();
		var event = (EventDto) result.getModelAndView().getModel().get("updatedEvent");

		assertEquals(1L, event.getId());
		assertEquals("New Title", event.getTitle());
		assertEquals(DATE, event.getDate());
	}

	@Test
	void updateEventTest_WithNotExistingId() throws Exception{
		mockMvc.perform(post("/event/update")
						.flashAttr("event", new EventDto(100L, "", null, PRICE)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteEventTest_WithExistingId() throws Exception{
		mockMvc.perform(post("/event/delete")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("deleteEventId"));
	}

	@Test
	void deleteEventTest_WithNotExistingId() throws Exception{
		mockMvc.perform(post("/event/delete")
						.param("id", "100"))
				.andExpect(status().isInternalServerError());
	}
}
