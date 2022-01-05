package org.example.controller;

import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:drop-tables.sql", "classpath:init-user.sql"})
@SpringBootTest
@Transactional
class UserControllerTest {

	private static final String USER_NAME = "Jules Mcnally";
	private static final String USER_EMAIL = "Jules_Mcnally8158@extex.org";

	private MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext wac){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Sql(value = { "classpath:drop-tables.sql" })
	@Sql(value = { "classpath:create-tables.sql" })
	@Test
	void testCreateUser_WithUniqueEmail() throws Exception{
		var result = mockMvc.perform(post("/user")
						.flashAttr("user", new User(0L, USER_NAME, "uniquemail@mail.com")))
						.andExpect(status().isOk())
						.andExpect(model().attributeExists("createdUser"))
						.andReturn();
		var user = (User) result.getModelAndView().getModel().get("createdUser");
		assertEquals(USER_NAME, user.getName());
	}

	@Test
	void testCreateUser_WithExistingEmail() throws Exception{
		mockMvc.perform(post("/user")
						.flashAttr("user", new User(0L, USER_NAME, USER_EMAIL)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetUserById_WithExistingId() throws Exception{
		var result = mockMvc.perform(get("/user")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("userById"))
				.andReturn();
		var user = (User) result.getModelAndView().getModel().get("userById");
		assertEquals(USER_NAME, user.getName());
	}

	@Test
	void testGetUserById_WithNotExistingId() throws Exception{
		mockMvc.perform(get("/user")
						.param("id", "100"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetUserById_WithExistingEmail() throws Exception{
		var result = mockMvc.perform(get("/user/byEmail")
						.param("email", USER_EMAIL))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("userByEmail"))
				.andReturn();
		var user = (User) result.getModelAndView().getModel().get("userByEmail");
		assertEquals(USER_EMAIL, user.getEmail());
	}

	@Test
	void testGetUserById_WithNotExistingEmail() throws Exception{
		var result = mockMvc.perform(get("/user/byEmail")
						.param("email", "uniquemail@mail.com"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetUsersByName_WithExistingName() throws Exception{
		var result = mockMvc.perform(get("/user/byName")
						.param("name", USER_NAME)
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andReturn();
		var users = (List<User>) result.getModelAndView().getModel().get("users");
		assertEquals(USER_NAME, users.get(0).getName());
	}

	@Test
	void testGetUsersByName_WithNotExistingName() throws Exception{
		var result = mockMvc.perform(get("/user/byName")
						.param("name", "Not a name")
						.param("pageSize", "1")
						.param("pageNum", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andReturn();
		var users = (List<User>) result.getModelAndView().getModel().get("users");
		assertEquals(0, users.size());
	}

	@Test
	void updateUserTest_WithAllAttributesUpdate() throws Exception{
		var result = mockMvc.perform(post("/user/update")
						.flashAttr("user", new User(1L, "New Name", "newmail@mail.com")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedUser"))
				.andReturn();
		var user = (User) result.getModelAndView().getModel().get("updatedUser");

		assertEquals(1L, user.getId());
		assertEquals("New Name", user.getName());
		assertEquals("newmail@mail.com", user.getEmail());
	}

	@Test
	void updateUserTest_WithoutUpdatedName() throws Exception{
		var result = mockMvc.perform(post("/user/update")
						.flashAttr("user", new User(1L, "", "newmail@mail.com")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedUser"))
				.andReturn();
		var user = (User) result.getModelAndView().getModel().get("updatedUser");

		assertEquals(1L, user.getId());
		assertEquals(USER_NAME, user.getName());
		assertEquals("newmail@mail.com", user.getEmail());
	}

	@Test
	void updateUserTest_WithoutUpdatedEmail() throws Exception{
		var result = mockMvc.perform(post("/user/update")
						.flashAttr("user", new User(1L, "New Name", "")))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("updatedUser"))
				.andReturn();
		var user = (User) result.getModelAndView().getModel().get("updatedUser");

		assertEquals(1L, user.getId());
		assertEquals("New Name", user.getName());
		assertEquals(USER_EMAIL, user.getEmail());
	}

	@Test
	void updateUserTest_WithNotUniqueEmail() throws Exception{
		mockMvc.perform(post("/user/update")
						.flashAttr("user", new User(2L, "New Name", USER_EMAIL)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateUserTest_WithNotExistingId() throws Exception{
		mockMvc.perform(post("/user/update")
						.flashAttr("user", new User(100L, USER_NAME, USER_EMAIL)))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteUserTest_WithExistingId() throws Exception{
		var result = mockMvc.perform(post("/user/delete")
						.param("id", "1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("deleteUserId"))
				.andReturn();

		var deletedUserId = (Long) result.getModelAndView().getModel().get("deleteUserId");
		assertEquals(1L, deletedUserId);
	}

	@Test
	void deleteUserTest_WithNotExistingId() throws Exception{
		mockMvc.perform(post("/user/delete")
						.param("id", "100"))
				.andExpect(status().isInternalServerError());
	}
}
