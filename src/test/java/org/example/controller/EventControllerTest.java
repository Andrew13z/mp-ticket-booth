package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EventDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.example.util.TestUtils.DEFAULT_EVENT_DATE;
import static org.example.util.TestUtils.DEFAULT_EVENT_TITLE;
import static org.example.util.TestUtils.DEFAULT_TICKET_PRICE;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.NEW_EVENT_DATE;
import static org.example.util.TestUtils.NEW_EVENT_TITLE;
import static org.example.util.TestUtils.NEW_TICKET_PRICE;
import static org.example.util.TestUtils.NOT_EXISTING_EVENT_DATE;
import static org.example.util.TestUtils.NOT_EXISTING_EVENT_TITLE;
import static org.example.util.TestUtils.NOT_EXISTING_ID;
import static org.example.util.TestUtils.SLASH;
import static org.example.util.TestUtils.createDefaultEventDto;
import static org.example.util.TestUtils.createEventDtoWithoutId;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventControllerTest {

	private static final String CONTROLLER_PATH = "/events";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateEvent_WithValidData() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createEventDtoWithoutId())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.title").value(DEFAULT_EVENT_TITLE))
				.andExpect(jsonPath("$.date").value(DEFAULT_EVENT_DATE.toString()))
				.andExpect(jsonPath("$.ticketPrice").value(DEFAULT_TICKET_PRICE));
	}

	@Test
	void testCreateEvent_WithEventIdNotNull() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createDefaultEventDto())))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateEvent_WithBlankEventTitle() throws Exception {
		var eventDto = createEventDtoWithoutId();
		eventDto.setTitle("");
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateEvent_WithNullDate() throws Exception {
		var eventDto = createEventDtoWithoutId();
		eventDto.setDate(null);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateEvent_WithDateInThePast() throws Exception {
		var eventDto = createEventDtoWithoutId();
		eventDto.setDate(LocalDate.now().minusDays(1));
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateEvent_WithNegativeTicketPrice() throws Exception {
		var eventDto = createEventDtoWithoutId();
		eventDto.setTicketPrice(BigDecimal.valueOf(-1L));
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(eventDto)))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testGetEventById_WithExistingId() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + SLASH + ID_ONE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());
	}

	@Test
	void testGetEventById_WithNotExistingId() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetEventsByTitle_WithExistingTitle() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(createEventDtoWithoutId())));
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("title", DEFAULT_EVENT_TITLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].title").value(DEFAULT_EVENT_TITLE));
	}

	@Test
	void testGetEventsByTitle_WithNotExistingTitle() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("title", NOT_EXISTING_EVENT_TITLE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void testGetEventsByDate_WithExistingDate() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(createEventDtoWithoutId())));
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("date", DEFAULT_EVENT_DATE.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].date").value(DEFAULT_EVENT_DATE.toString()));
	}

	@Test
	void testGetEventsByDate_WithNotExistingDate() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("date", NOT_EXISTING_EVENT_DATE.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void updateEventTest_WithAllAttributesUpdated() throws Exception {
		var createdEventDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createEventDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdEventDto = mapper.readValue(createdEventDtoJson, EventDto.class);
		createdEventDto.setTitle(NEW_EVENT_TITLE);
		createdEventDto.setDate(NEW_EVENT_DATE);
		createdEventDto.setTicketPrice(NEW_TICKET_PRICE);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdEventDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdEventDto)))
				.andExpect(jsonPath("$.title").value(NEW_EVENT_TITLE))
				.andExpect(jsonPath("$.date").value(NEW_EVENT_DATE.toString()))
				.andExpect(jsonPath("$.ticketPrice").value(NEW_TICKET_PRICE));
	}

	@Test
	void updateEventTest_WithoutUpdatedTitle() throws Exception {
		var createdEventDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createEventDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdEventDto = mapper.readValue(createdEventDtoJson, EventDto.class);
		createdEventDto.setDate(NEW_EVENT_DATE);
		createdEventDto.setTicketPrice(NEW_TICKET_PRICE);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdEventDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdEventDto)))
				.andExpect(jsonPath("$.title").value(DEFAULT_EVENT_TITLE))
				.andExpect(jsonPath("$.date").value(NEW_EVENT_DATE.toString()))
				.andExpect(jsonPath("$.ticketPrice").value(NEW_TICKET_PRICE));
	}

	@Test
	void updateEventTest_WithoutUpdatedDate() throws Exception {
		var createdEventDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createEventDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdEventDto = mapper.readValue(createdEventDtoJson, EventDto.class);
		createdEventDto.setTitle(NEW_EVENT_TITLE);
		createdEventDto.setTicketPrice(NEW_TICKET_PRICE);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdEventDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdEventDto)))
				.andExpect(jsonPath("$.title").value(NEW_EVENT_TITLE))
				.andExpect(jsonPath("$.date").value(DEFAULT_EVENT_DATE.toString()))
				.andExpect(jsonPath("$.ticketPrice").value(NEW_TICKET_PRICE));
	}

	@Test
	void updateEventTest_WithoutUpdatedTicketPrice() throws Exception {
		var createdEventDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createEventDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdEventDto = mapper.readValue(createdEventDtoJson, EventDto.class);
		createdEventDto.setTitle(NEW_EVENT_TITLE);
		createdEventDto.setDate(NEW_EVENT_DATE);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdEventDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdEventDto)))
				.andExpect(jsonPath("$.title").value(NEW_EVENT_TITLE))
				.andExpect(jsonPath("$.date").value(NEW_EVENT_DATE.toString()))
				.andExpect(jsonPath("$.ticketPrice").value(DEFAULT_TICKET_PRICE));
	}

	@Test
	void updateEventTest_WithNotExistingId() throws Exception {
		mockMvc.perform(put(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createDefaultEventDto())))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteEventTest_WithExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ID_ONE)))
				.andExpect(status().isOk());
	}

	@Test
	void deleteEventTest_WithNotExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(NOT_EXISTING_ID)))
				.andExpect(status().isInternalServerError());
	}
}
