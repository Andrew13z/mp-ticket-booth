package org.example.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TicketDto;
import org.example.facade.BookingFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageConsumer {

	public static final Logger logger = LoggerFactory.getLogger(JmsMessageConsumer.class);

	@Autowired
	private BookingFacade bookingFacade;

	@Autowired
	private ObjectMapper objectMapper;

	@JmsListener(destination = "mp-ticket-queue")
	public void processMessageFromQueue(Message<String> message) throws JsonProcessingException {
		var ticketDto = objectMapper.readValue(message.getPayload(), TicketDto.class);
		logger.info("Processing message from mp-ticket-queue");
		bookingFacade.bookTicket(ticketDto.getUser().getId(),
								ticketDto.getEvent().getId(),
								ticketDto.getCategory(),
								ticketDto.getPlace());
	}
}
