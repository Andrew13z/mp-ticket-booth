package org.example.dao;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.example.model.User;
import org.example.repository.InMemoryStorage;
import org.example.repository.TicketInMemoryStorage;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketRepositoryTest {

	private final long ID_1 = 1L;
	private final long EVENT_ID_1 = 1L;
	private final long USER_ID_1 = 1L;
	private final Ticket.Category CATEGORY_1 = Ticket.Category.STANDARD;
	private final int PLACE_1 = 1;

	private final long ID_2 = 2L;
	private final long EVENT_ID_2 = 2L;
	private final long USER_ID_2 = 2L;
	private final Ticket.Category CATEGORY_2 = Ticket.Category.PREMIUM;
	private final int PLACE_2 = 2;

	@Mock
	private TicketInMemoryStorage mockStorage;

	@InjectMocks
	private TicketRepository repository;

	@Test
	void saveTest(){
		var ticketMap = new HashMap<Long, Ticket>();

		when(mockStorage.getData()).thenReturn(ticketMap);
		when(mockStorage.getIndex()).thenReturn(ID_1);

		var savedEvent = repository.save(createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1));

		assertEquals(1, ticketMap.size());
		assertEquals(ID_1, savedEvent.getId());
	}

	@Test
	void getTestWithExistingId() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1)));
		Optional<Ticket> ticket = repository.get(ID_1);
		assertTrue(ticket.isPresent());
		assertEquals(ID_1, ticket.get().getId());
	}

	@Test
	void getTestWithNotExistingId() {
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1)));
		Optional<Ticket> ticket = repository.get(ID_2);
		assertTrue(ticket.isEmpty());
	}

	@Test
	void getAllTest(){
		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1),
														 ID_2, createTicket(ID_2, USER_ID_1, EVENT_ID_2, CATEGORY_2, PLACE_2)));
		var ticketList = repository.getAll();
		assertEquals(2, ticketList.size());
	}

	@Test
	void deleteTestWithExistingId(){
		var ticketMap = new HashMap<Long, Ticket>();
		ticketMap.put(ID_1, createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1));
		ticketMap.put(ID_2, createTicket(ID_2, USER_ID_2, EVENT_ID_2, CATEGORY_2, PLACE_2));

		when(mockStorage.getData()).thenReturn(ticketMap);

		assertTrue(repository.delete(ID_1));
		assertEquals(1, ticketMap.size());
	}

	@Test
	void deleteTestWithNotExistingId(){
		var ticketMap = new HashMap<Long, Ticket>();
		ticketMap.put(ID_2, createTicket(ID_2, USER_ID_2, EVENT_ID_2, CATEGORY_2, PLACE_2));

		when(mockStorage.getData()).thenReturn(ticketMap);

		assertFalse(repository.delete(ID_1));
		assertEquals(1, ticketMap.size());
	}

	@Test
	void getDataForUserPaginationTest() {
		User user = new User(USER_ID_1, null, null);

		when(mockStorage.getData()).thenReturn(Map.of(ID_1, createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1),
														ID_2, createTicket(ID_2,USER_ID_2, EVENT_ID_2, CATEGORY_2, PLACE_2),
														3L, createTicket(3L, USER_ID_1, 3L, Ticket.Category.BAR, 3),
														4L, createTicket(4L, USER_ID_1, 4L, Ticket.Category.BAR, 4)));

		var ticketListFirstPage = repository.getBookedTickets(user, 2, 1);
		var ticketListSecondPage = repository.getBookedTickets(user, 2, 2);

		assertEquals(2, ticketListFirstPage.size());
		assertEquals(USER_ID_1, ticketListFirstPage.get(0).getUserId());
		assertEquals(USER_ID_1, ticketListFirstPage.get(1).getUserId());

		assertEquals(1, ticketListSecondPage.size());
		assertEquals(USER_ID_1, ticketListSecondPage.get(0).getUserId());
	}

	@Test
	void getDataForEventPaginationTest() {
		Event event = new Event(EVENT_ID_1, null, null);

		var ticket = createTicket(ID_1, USER_ID_1, EVENT_ID_1, CATEGORY_1, PLACE_1);
		var ticket3 = createTicket(3L, 3L, EVENT_ID_1, Ticket.Category.BAR, 3);
		var map = Map.of(ID_1, ticket,
				ID_2, createTicket(ID_2, USER_ID_2, EVENT_ID_2, CATEGORY_2, PLACE_2),
				3L, ticket3,
				4L, createTicket(4L, 4L, EVENT_ID_1, Ticket.Category.BAR, 4));
		when(mockStorage.getData()).thenReturn(map);

		var ticketListFirstPage = repository.getBookedTickets(event, 2, 1);
		var ticketListSecondPage = repository.getBookedTickets(event, 2, 2);

		assertEquals(2, ticketListFirstPage.size());
		assertEquals(EVENT_ID_1, ticketListFirstPage.get(0).getEventId());
		assertEquals(EVENT_ID_1, ticketListFirstPage.get(1).getEventId());

		assertEquals(1, ticketListSecondPage.size());
		assertEquals(EVENT_ID_1, ticketListSecondPage.get(0).getEventId());
	}

	private Ticket createTicket(long id, long userId, long eventId, Ticket.Category category, int place) {
		return new TicketBuilder()
				.setId(id)
				.setUserId(userId)
				.setEventId(eventId)
				.setCategory(category)
				.setPlace(place)
				.createTicket();
	}
}
