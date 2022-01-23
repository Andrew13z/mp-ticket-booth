package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Import(TestConfig.class)
class AccountControllerTest {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testRefillAccount_WithExistingUserId() throws Exception{
		mockMvc.perform(patch("/accounts/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(BigDecimal.TEN)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void testRefillAccount_WithNotExistingUserId() throws Exception{
		mockMvc.perform(patch("/accounts/1000")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(BigDecimal.TEN)))
				.andExpect(status().isNotFound());
	}
}
