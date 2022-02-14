package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.example.util.TestUtils.DEFAULT_USER_EMAIL;
import static org.example.util.TestUtils.DEFAULT_USER_NAME;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.NEW_USER_EMAIL;
import static org.example.util.TestUtils.NEW_USER_NAME;
import static org.example.util.TestUtils.NOT_EXISTING_ID;
import static org.example.util.TestUtils.PREEXISTING_USER_EMAIL;
import static org.example.util.TestUtils.PREEXISTING_USER_NAME;
import static org.example.util.TestUtils.SLASH;
import static org.example.util.TestUtils.createDefaultUserDto;
import static org.example.util.TestUtils.createUserDtoWithoutId;
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
class UserControllerTest {

	private static final String CONTROLLER_PATH = "/users";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateUser_WithUniqueEmail() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value(DEFAULT_USER_NAME))
				.andExpect(jsonPath("$.email").value(DEFAULT_USER_EMAIL));
	}

	@Test
	void testCreateUser_WithExistingEmail() throws Exception {
		var userDto = createUserDtoWithoutId();
		userDto.setEmail(PREEXISTING_USER_EMAIL);

		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateUser_WithUserIdNotNull() throws Exception {
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createDefaultUserDto())))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateUser_WithBlankUserName() throws Exception {
		var userDto = createUserDtoWithoutId();
		userDto.setName("");
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testCreateUser_WithInvalidEmail() throws Exception {
		var userDto = createUserDtoWithoutId();
		userDto.setEmail("not-email");
		mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(userDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetUserById_WithExistingId() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + SLASH + ID_ONE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists());
	}

	@Test
	void testGetUserById_WithNotExistingId() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetUserByEmail_WithExistingEmail() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + "/byEmail")
						.param("email", PREEXISTING_USER_EMAIL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value(PREEXISTING_USER_NAME))
				.andExpect(jsonPath("$.email").value(PREEXISTING_USER_EMAIL));
	}

	@Test
	void testGetUserByEmail_WithNotExistingEmail() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + "/byEmail")
						.param("email", DEFAULT_USER_EMAIL))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetUsersByName_WithExistingName() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + "/byName")
						.param("name", PREEXISTING_USER_NAME))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].name").value(PREEXISTING_USER_NAME));
	}

	@Test
	void testGetUsersByName_WithNotExistingName() throws Exception {
		mockMvc.perform(get(CONTROLLER_PATH + "/byName")
						.param("name", DEFAULT_USER_NAME))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content", hasSize(0)));
	}

	@Test
	void updateUserTest_WithAllAttributesUpdate() throws Exception {
		var createdUserDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);
		createdUserDto.setName(NEW_USER_NAME);
		createdUserDto.setEmail(NEW_USER_EMAIL);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdUserDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdUserDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(NEW_USER_NAME))
				.andExpect(jsonPath("$.email").value(NEW_USER_EMAIL));
	}

	@Test
	void updateUserTest_WithoutUpdatedName() throws Exception {
		var createdUserDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);
		createdUserDto.setEmail(NEW_USER_EMAIL);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdUserDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdUserDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(DEFAULT_USER_NAME))
				.andExpect(jsonPath("$.email").value(NEW_USER_EMAIL));
	}

	@Test
	void updateUserTest_WithoutUpdatedEmail() throws Exception {
		var createdUserDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);
		createdUserDto.setName(NEW_USER_NAME);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdUserDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdUserDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(NEW_USER_NAME))
				.andExpect(jsonPath("$.email").value(DEFAULT_USER_EMAIL));
	}

	@Test
	void updateUserTest_WithNotUniqueEmail() throws Exception {
		var createdUserDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);
		createdUserDto.setEmail(PREEXISTING_USER_EMAIL);

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdUserDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdUserDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateUserTest_WithInvalidEmail() throws Exception {
		var createdUserDtoJson = mockMvc.perform(post(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andReturn().getResponse().getContentAsString();

		var createdUserDto = mapper.readValue(createdUserDtoJson, UserDto.class);
		createdUserDto.setEmail("not-email");

		mockMvc.perform(put(CONTROLLER_PATH + SLASH + createdUserDto.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createdUserDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateUserTest_WithNotExistingId() throws Exception {
		mockMvc.perform(put(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(createUserDtoWithoutId())))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteUserTest_WithExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(ID_ONE)))
				.andExpect(status().isOk());
	}

	@Test
	void deleteUserTest_WithNotExistingId() throws Exception {
		mockMvc.perform(delete(CONTROLLER_PATH)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(NOT_EXISTING_ID)))
				.andExpect(status().isInternalServerError());
	}
}
