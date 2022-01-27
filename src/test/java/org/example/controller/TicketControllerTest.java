package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.example.util.TestUtils.DEFAULT_TICKET_PLACE;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.NOT_EXISTING_ID;
import static org.example.util.TestUtils.createTicketDtoFotTicketCreateOperation;
import static org.example.util.TestUtils.createUserDtoWithoutId;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-without-jmslistener")//todo temporary measure because one test is failing in TicketControllerTest
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

	@Test
	void testCreateTicket_WithId() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.setId(ID_ONE);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateTicket_WithoutUserId() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.getUser().setId(null);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateTicket_WithNotExistingUser() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.getUser().setId(NOT_EXISTING_ID);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateTicket_WithoutEventId() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.getEvent().setId(null);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateTicket_WithNotExistingEvent() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.getEvent().setId(NOT_EXISTING_ID);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateTicket_WithoutCategory() throws Exception {
		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.setCategory(null);
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateTicket_WithoutInsufficientAccountBalance() throws Exception {
		var createdUserDtoJson =mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
						.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);

		var ticketDto = createTicketDtoFotTicketCreateOperation();
		ticketDto.getUser().setId(createdUserDto.getId());

		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ticketDto)))
				.andExpect(status().isBadRequest());
	}


	@Test
	void testGetTicketsByUser_WithExistingUser() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("userId", ID_ONE.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].user.id").value(ID_ONE));
	}

	@Test
	void testGetTicketsByUser_WithNotExistingUser() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("userId", NOT_EXISTING_ID.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void testGetTicketsByEvent_WithExistingEvent() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("eventId", ID_ONE.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].event.id").value(ID_ONE))
				.andExpect(jsonPath("$[1].event.id").value(ID_ONE))
				.andExpect(jsonPath("$[2].event.id").value(ID_ONE));
	}

	@Test
	void testGetTicketsByEvent_WithNotExistingEvent() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.param("userId", NOT_EXISTING_ID.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void testGetTicketsByUserAsPdf() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH)
						.accept(MediaType.APPLICATION_PDF_VALUE)
						.param("userId", ID_ONE.toString()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_PDF_VALUE));
	}

	@Test
	void testBatchBookTicketsFromFile() throws Exception {
		var fileContent = "<java.util.ArrayList>\n" +
				"\t<org.example.model.Ticket>\n" +
				"        <user><id>4</id></user>\n" +
				"        <event><id>2</id></event>\n" +
				"        <category>STANDARD</category>\n" +
				"        <place>45</place>\n" +
				"    </org.example.model.Ticket>\n" +
				"    <org.example.model.Ticket>\n" +
				"        <user><id>8</id></user>\n" +
				"        <event><id>2</id></event>\n" +
				"        <category>PREMIUM</category>\n" +
				"        <place>425</place>\n" +
				"    </org.example.model.Ticket>" +
				"</java.util.ArrayList>";
		var multipartFile = new MockMultipartFile("tickets.xml", "", MediaType.APPLICATION_XML_VALUE, fileContent.getBytes());
		mockMvc.perform(MockMvcRequestBuilders.multipart(CONTROLLER_PATH + "/batch")
						.file("file", multipartFile.getBytes())
						.characterEncoding("UTF-8"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].user.id").value(4))
				.andExpect(jsonPath("$[0].event.id").value(2))
				.andExpect(jsonPath("$[1].user.id").value(8))
				.andExpect(jsonPath("$[1].event.id").value(2));
	}

	@Test
	void deleteTicketTest_WithExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ID_ONE)))
				.andExpect(status().isOk());
	}

	@Test
	void deleteTicketTest_WithNotExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(NOT_EXISTING_ID)))
				.andExpect(status().isNotFound());
	}
}
