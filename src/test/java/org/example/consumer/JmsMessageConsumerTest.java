package org.example.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.example.repository.AccountRepository;
import org.example.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

import static org.example.util.TestUtils.FIRST_EVENT_TICKET_PRICE;
import static org.example.util.TestUtils.FIRST_USER_ACCOUNT_BALANCE;
import static org.example.util.TestUtils.ID_ONE;
import static org.example.util.TestUtils.NEXT_TICKET_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class JmsMessageConsumerTest {

	private final AtomicReference<String> testMessageHolder = new AtomicReference<>();

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Test
	void testProcessMessageFromQueue() throws Exception{
		var userDto = new UserDto(1L, null, null);
		var eventDto = new EventDto(1L, null, null, null);
		var ticketDto = new TicketDto(null, userDto, eventDto, Category.STANDARD, 1);
		var message = mapper.writeValueAsString(ticketDto);
		jmsTemplate.convertAndSend("mp-ticket-queue", message);

		//wait for ticket processing
		Thread.sleep(1000);

		var ticket = ticketRepository.findById(NEXT_TICKET_ID).get();
		var account = accountRepository.findById(ID_ONE).get();

		assertEquals(ID_ONE, ticket.getUser().getId());
		assertEquals(ID_ONE, ticket.getEvent().getId());
		assertEquals(Category.STANDARD, ticket.getCategory());
		assertEquals(1, ticket.getPlace());

		assertEquals(0, account.getBalance().compareTo(FIRST_USER_ACCOUNT_BALANCE.subtract(FIRST_EVENT_TICKET_PRICE)));
	}
}
