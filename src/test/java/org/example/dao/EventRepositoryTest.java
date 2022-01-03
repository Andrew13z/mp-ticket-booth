package org.example.dao;


import org.example.exception.EntityNotFoundException;
import org.example.model.Event;
import org.example.repository.EventInMemoryStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventRepositoryTest {

	private final long ID_1 = 1L;
	private final String TITLE_1 = "Event 1";
	private final LocalDate DATE_1 = LocalDate.of(2022, 1, 1);

	private final long ID_2 = 2L;
	private final String TITLE_2 = "Event 2";
	private final LocalDate DATE_2 = LocalDate.of(2022, 2, 2);

	private final long ID_ZERO = 0;
	private final BigDecimal PRICE_ZERO = BigDecimal.ZERO;

	@Mock
	private EventInMemoryStorage mockStorage;

	@InjectMocks
	private EventRepository repository;

	@Test
	void saveTest() {
		var eventMap = new HashMap<Long, Event>();

		when(mockStorage.getData()).thenReturn(eventMap);
		when(mockStorage.getIndex()).thenReturn(ID_1);

		var savedEvent = repository.save(createEvent(ID_ZERO, TITLE_1, DATE_1, PRICE_ZERO));

		assertEquals(1, eventMap.size());
		assertEquals(ID_1, savedEvent.getId());
	}

	@Test
	void updateEventTestWithExistingId() {
		var oldEvent = createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO);
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, oldEvent));

		var newEvent = createEvent(ID_1, TITLE_2, DATE_2, PRICE_ZERO);
		var updatedEvent = repository.updateEvent(newEvent);

		assertEquals(ID_1, updatedEvent.getId());
		assertEquals(TITLE_2, updatedEvent.getTitle());
		assertEquals(DATE_2, updatedEvent.getDate());
	}

	@Test
	void updateEventTestWithNotExistingId() {
		var newEvent = createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO);

		assertThrowsExactly(EntityNotFoundException.class,
				() -> repository.updateEvent(newEvent),
				"Event not found by id: 1");
	}

	@Test
	void getTestWithExistingId() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO)));
		Optional<Event> event = repository.get(ID_1);
		assertTrue(event.isPresent());
		assertEquals(ID_1, event.get().getId());
	}

	@Test
	void getTestWithNotExistingId() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO)));
		Optional<Event> event = repository.get(ID_2);
		assertTrue(event.isEmpty());
	}

	@Test
	void getAllTest() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO),
				ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO)));
		var eventList = repository.getAll();
		assertEquals(2, eventList.size());
	}

	@Test
	void deleteTestWithExistingId() {
		var eventMap = new HashMap<Long, Event>();
		eventMap.put(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO));
		eventMap.put(ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO));

		when(mockStorage.getData()).thenReturn(eventMap);

		assertTrue(repository.delete(ID_1));
		assertEquals(1, eventMap.size());
	}

	@Test
	void deleteTestWithNotExistingId() {
		var eventMap = new HashMap<Long, Event>();
		eventMap.put(ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO));

		when(mockStorage.getData()).thenReturn(eventMap);

		assertFalse(repository.delete(ID_1));
		assertEquals(1, eventMap.size());
	}

	@Test
	void getEventsByTitleTest() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO),
				ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO)));
		var eventList = repository.getEventsByTitle(TITLE_1, 2, 1);

		assertEquals(1, eventList.size());
		assertEquals(TITLE_1, eventList.get(0).getTitle());
	}

	@Test
	void getEventsByTitlePaginationTest() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO),
				ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO),
				3L, createEvent(3L, TITLE_1, LocalDate.of(2022, 3, 3), PRICE_ZERO),
				4L, createEvent(4L, TITLE_1, LocalDate.of(2022, 4, 4), PRICE_ZERO)));

		var eventListFirstPage = repository.getEventsByTitle(TITLE_1, 2, 1);
		var eventListSecondPage = repository.getEventsByTitle(TITLE_1, 2, 2);

		assertEquals(2, eventListFirstPage.size());
		assertEquals(TITLE_1, eventListFirstPage.get(0).getTitle());
		assertEquals(TITLE_1, eventListFirstPage.get(1).getTitle());

		assertEquals(1, eventListSecondPage.size());
		assertEquals(TITLE_1, eventListSecondPage.get(0).getTitle());
	}

	@Test
	void getEventsByDateTest() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO),
				ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO)));
		var eventList = repository.getEventsForDay(DATE_1, 2, 1);

		assertEquals(1, eventList.size());
		assertEquals(DATE_1, eventList.get(0).getDate());
	}

	@Test
	void getEventsByDatePaginationTest() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createEvent(ID_1, TITLE_1, DATE_1, PRICE_ZERO),
				ID_2, createEvent(ID_2, TITLE_2, DATE_2, PRICE_ZERO),
				3L, createEvent(3L, "Title 3", DATE_1, PRICE_ZERO),
				4L, createEvent(4L, "Title 4", DATE_1, PRICE_ZERO)));

		var eventListFirstPage = repository.getEventsForDay(DATE_1, 2, 1);
		var eventListSecondPage = repository.getEventsForDay(DATE_1, 2, 2);

		assertEquals(2, eventListFirstPage.size());
		assertEquals(DATE_1, eventListFirstPage.get(0).getDate());
		assertEquals(DATE_1, eventListFirstPage.get(1).getDate());

		assertEquals(1, eventListSecondPage.size());
		assertEquals(DATE_1, eventListSecondPage.get(0).getDate());
	}

	private Event createEvent(long id, String title, LocalDate date, BigDecimal price) {
		return new Event(id, title, date, price);
	}
}
