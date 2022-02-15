package org.example.service.impl;

import org.example.repository.UserRepository;
import org.example.exception.EntityNotFoundException;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.example.util.TestUtils.DEFAULT_USER_EMAIL;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.createDefaultUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository mockRepository;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void getUserByIdTestWithExistingId() {
		when(mockRepository.findByIdWithCache(ID_ONE)).thenReturn(Optional.of(createDefaultUser()));
		var user = userService.getUserById(ID_ONE);
		assertEquals(ID_ONE, user.getId());
	}

	@Test
	void getUserByIdTestWithNonExistingId() {
		when(mockRepository.findByIdWithCache(ID_ONE)).thenReturn(Optional.empty());
		var exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(ID_ONE));
		assertEquals("User not found by id: 1", exception.getMessage());
	}

	@Test
	void getUserByEmailTestWithExistingId() {
		when(mockRepository.findByEmail(DEFAULT_USER_EMAIL)).thenReturn(Optional.of(createDefaultUser()));
		var user = userService.getUserByEmail(DEFAULT_USER_EMAIL);
		assertEquals(DEFAULT_USER_EMAIL, user.getEmail());
	}

	@Test
	void getUserByEmailTestWithNonExistingId() {
		when(mockRepository.findByEmail(DEFAULT_USER_EMAIL)).thenReturn(Optional.empty());
		var exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(DEFAULT_USER_EMAIL));
		assertEquals("User not found by email: " + DEFAULT_USER_EMAIL, exception.getMessage());
	}

}
