package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.NOT_EXISTING_ID;
import static org.example.util.TestUtils.SLASH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Rollback
class AccountControllerTest {

	private static final String CONTROLLER_PATH = "/accounts";

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testRefillAccount_WithValidDate() throws Exception {
		mockMvc.perform(patch(CONTROLLER_PATH + SLASH + ID_ONE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(BigDecimal.TEN)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void testRefillAccount_WithNotExistingUserId() throws Exception {
		mockMvc.perform(patch(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(BigDecimal.TEN)))
				.andExpect(status().isNotFound());
	}

	@Test
	void testRefillAccount_WithNegativeRefillSum() throws Exception{
		mockMvc.perform(patch(CONTROLLER_PATH + SLASH + ID_ONE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(BigDecimal.valueOf(-1L))))
				.andExpect(status().isBadRequest());
	}
}
