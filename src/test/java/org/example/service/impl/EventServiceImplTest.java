package org.example.service.impl;

import org.example.repository.EventRepository;
import org.example.exception.EntityNotFoundException;
import org.example.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.createDefaultEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

	@Mock
	private EventRepository mockDao;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private EventServiceImpl eventService;

	@Test
	void getEventByIdTestWithExistingId() {
		when(mockDao.findByIdWithCache(ID_ONE)).thenReturn(Optional.of(createDefaultEvent()));
		var eventById = eventService.getEventById(ID_ONE);
		assertEquals(ID_ONE, eventById.getId());
	}

	@Test
	void getEventByIdTestWithNonExistingId() {
		when(mockDao.findByIdWithCache(ID_ONE)).thenReturn(Optional.empty());
		var exception = assertThrows(EntityNotFoundException.class, () -> eventService.getEventById(ID_ONE));
		assertEquals("Event not found by id: 1", exception.getMessage());
	}

}
