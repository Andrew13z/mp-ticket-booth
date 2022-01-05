package org.example.controller;

import org.example.dto.AccountDto;
import org.example.model.Account;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = { "classpath:drop-tables.sql" })
@Sql(value = { "classpath:init-user.sql" })
@SpringBootTest
class AccountControllerTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext wac){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void testRefillAccount_WithExistingUserId() throws Exception{
		var result = mockMvc.perform(post("/account")
						.param("userId", "1")
						.param("refillSum", "100"))
				.andExpect(status().isOk())
				.andReturn();

		var account = (AccountDto) result.getModelAndView().getModel().get("account");
		assertEquals(0, BigDecimal.valueOf(200).compareTo(account.getBalance()));
	}

	@Test
	void testRefillAccount_WithNotExistingUserId() throws Exception{
		mockMvc.perform(post("/account")
						.param("userId", "100")
						.param("refillSum", "100"))
				.andExpect(status().isNotFound());
	}

}
