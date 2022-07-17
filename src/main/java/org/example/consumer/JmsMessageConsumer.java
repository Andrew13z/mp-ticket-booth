package org.example.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TicketDto;
import org.example.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageConsumer {

	public static final Logger logger = LoggerFactory.getLogger(JmsMessageConsumer.class);

	private final TicketService ticketService;

	private final ObjectMapper objectMapper;

	@Autowired
	public JmsMessageConsumer(TicketService ticketService, ObjectMapper objectMapper) {
		this.ticketService = ticketService;
		this.objectMapper = objectMapper;
	}

	@JmsListener(destination = "mp-ticket-queue")
	public void processMessageFromQueue(Message<String> message) throws JsonProcessingException {
		var ticketDto = objectMapper.readValue(message.getPayload(), TicketDto.class);
		logger.info("Processing message from mp-ticket-queue");
		ticketService.bookTicket(ticketDto.getUser().getId(),
				ticketDto.getEvent().getId(),
				ticketDto.getCategory(),
				ticketDto.getPlace());
	}
}
