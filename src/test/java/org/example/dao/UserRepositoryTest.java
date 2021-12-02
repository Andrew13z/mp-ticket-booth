package org.example.dao;

import org.example.exception.EntityNotFoundException;
import org.example.model.User;
import org.example.repository.InMemoryStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

	private final long ID_1 = 1L;
	private final String NAME_1 = "Name 1";
	private final String EMAIL_1 = "email1@mail.com";

	private final long ID_2 = 2L;
	private final String NAME_2 = "Name 2";
	private final String EMAIL_2 = "email2@mail.com";

	private final long ID_ZERO = 0;

	private final String EMAIL_NOT_UNIQUE_MESSAGE = "User email must be unique";

	@Mock
	private InMemoryStorage mockStorage;

	@InjectMocks
	private UserRepository repository;

	@Test
	void saveTestWithUniqueEmail() {
		var user = createUser(ID_ZERO, NAME_1, EMAIL_1);

		var userMap = new HashMap<Long, User>();

		when(mockStorage.getUsers()).thenReturn(userMap);
		when(mockStorage.getUserIndex()).thenReturn(ID_1);

		var savedUser = repository.save(user);
		assertEquals(1, userMap.size());
		assertEquals(savedUser.getId(), ID_1);
	}

	@Test
	void saveTestWithExistingEmail() {
		var user = createUser(ID_ZERO, NAME_1, EMAIL_1);
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_2, createUser(ID_2, NAME_2, EMAIL_1)));
		assertThrowsExactly(IllegalArgumentException.class,
							() -> repository.save(user),
		 					EMAIL_NOT_UNIQUE_MESSAGE);
	}

	@Test
	void updateUserNameTestWithValidIdAndEmail(){
		var oldUser = createUser(ID_1, NAME_1, EMAIL_1);
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));

		var newUser = createUser(ID_1, NAME_2, EMAIL_1);

		var updatedUser = repository.update(newUser);

		assertEquals(NAME_2, updatedUser.getName());
	}

	@Test
	void updateUserEmailTestWithValidIdAndEmail(){
		var oldUser = createUser(ID_1, NAME_1, EMAIL_1);
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));

		var newUser = createUser(ID_1, NAME_1, EMAIL_2);
		var updatedUser = repository.update(newUser);

		assertEquals(EMAIL_2, updatedUser.getEmail());
	}

	@Test
	void updateUserEmailTestWithValidIdAndExistingEmail(){
		var oldUser = createUser(ID_1, NAME_1, EMAIL_1);
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1),
				ID_2, createUser(ID_2, NAME_2, EMAIL_2)));
		var newUser = createUser(ID_1, NAME_1, EMAIL_2);

		assertThrowsExactly(IllegalArgumentException.class,
							() -> repository.update(newUser),
				EMAIL_NOT_UNIQUE_MESSAGE);
	}

	@Test
	void updateUserTestWithNotExistingId(){
		var newUser = createUser(ID_1, NAME_1, EMAIL_1);

		assertThrowsExactly(EntityNotFoundException.class,
							() -> repository.update(newUser),
					"User not found by id: 1");
	}

	@Test
	void getTestWithExistingId() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));
		Optional<User> user = repository.get(ID_1);
		assertTrue(user.isPresent());
		assertEquals(ID_1, user.get().getId());
	}

	@Test
	void getTestWithNotExistingId() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));
		Optional<User> user = repository.get(ID_2);
		assertTrue(user.isEmpty());
	}

	@Test
	void getAllTest(){
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1),
													   ID_2, createUser(ID_2, NAME_2, EMAIL_2)));
		var userList = repository.getAll();
		assertEquals(2, userList.size());
	}

	@Test
	void deleteTestWithExistingId(){
		var userMap = new HashMap<Long, User>();
		userMap.put(ID_1, createUser(ID_1, NAME_1, EMAIL_1));
		userMap.put(ID_2, createUser(ID_2, NAME_2, EMAIL_2));

		when(mockStorage.getUsers()).thenReturn(userMap);

		assertTrue(repository.delete(ID_1));
		assertEquals(1, userMap.size());
	}

	@Test
	void deleteTestWithNotExistingId(){
		var userMap = new HashMap<Long, User>();
		userMap.put(ID_2, createUser(ID_2, NAME_2, EMAIL_2));

		when(mockStorage.getUsers()).thenReturn(userMap);

		assertFalse(repository.delete(ID_1));
		assertEquals(1, userMap.size());
	}

	@Test
	void getUserByEmailTestWithExistingEmail() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));
		Optional<User> user = repository.getUserByEmail(EMAIL_1);
		assertTrue(user.isPresent());
		assertEquals(EMAIL_1, user.get().getEmail());
	}

	@Test
	void getUserByEmailTestWithNotExistingEmail() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1)));
		Optional<User> user = repository.getUserByEmail(EMAIL_2);
		assertTrue(user.isEmpty());
	}

	@Test
	void getUsersByNameTest() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1),
				ID_2, createUser(ID_2, NAME_2, EMAIL_2)));
		var userList = repository.getUsersByName(NAME_1, 2, 1);

		assertEquals(1, userList.size());
		assertEquals(NAME_1, userList.get(0).getName());
	}

	@Test
	void getUsersByNamePaginationTest() {
		when(mockStorage.getUsers()).thenReturn(Map.of(ID_1, createUser(ID_1, NAME_1, EMAIL_1),
													   ID_2, createUser(ID_2, NAME_2, EMAIL_2),
													   3L, createUser(3L, NAME_1, "email3@mail.com"),
													   4L, createUser(4L, NAME_1, "email4@mail.com")));

		var userListFirstPage = repository.getUsersByName(NAME_1, 2, 1);
		var userListSecondPage = repository.getUsersByName(NAME_1, 2, 2);

		assertEquals(2, userListFirstPage.size());
		assertEquals(NAME_1, userListFirstPage.get(0).getName());
		assertEquals(NAME_1, userListFirstPage.get(1).getName());

		assertEquals(1, userListSecondPage.size());
		assertEquals(NAME_1, userListSecondPage.get(0).getName());
	}












	private User createUser(long id, String name, String email) {
		return new User(id, name, email);
	}


}
