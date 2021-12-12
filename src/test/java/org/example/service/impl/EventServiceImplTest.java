package org.example.service.impl;

import org.example.dao.EventRepository;
import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

	private final long ID = 1L;
	private final String TITLE = "Event";
	private final LocalDate DATE = LocalDate.of(2022, 1, 1);

	@Mock
	private EventRepository mockDao;

	@InjectMocks
	private EventServiceImpl eventService;

	@Test
	void getEventByIdTestWithExistingId() {
		when(mockDao.get(ID)).thenReturn(Optional.of(new Event(ID, TITLE, DATE)));
		var eventById = eventService.getEventById(ID);
		assertEquals(eventById.getId(), ID);
	}

	@Test
	void getEventByIdTestWithNonExistingId() {
		when(mockDao.get(ID)).thenReturn(Optional.empty());
		var exception = assertThrows(EntityNotFoundException.class, () -> eventService.getEventById(ID));//todo
		assertEquals("Event not found by id: 1", exception.getMessage());
	}

}
