package org.example.repository;

import org.example.converter.TicketXmlConverter;
import org.example.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TicketInMemoryStorage extends InMemoryStorage<Ticket> {

	private static final Logger logger = LoggerFactory.getLogger(TicketInMemoryStorage.class);

	@Value("${tickets.source}")
	private Resource ticketsFile;

	private final TicketXmlConverter ticketXmlConverter;

	private Map<Long, Ticket> tickets = new HashMap<>();

	@Autowired
	public TicketInMemoryStorage(TicketXmlConverter ticketXmlConverter) {
		super(null);//todo
		this.ticketXmlConverter = ticketXmlConverter;
	}

	@PostConstruct
	private void postConstruct() {
		try {
			tickets = ticketXmlConverter.xmlToObject().stream()
					.collect(Collectors.toMap(Ticket::getId,
											  Function.identity()));
		} catch (IOException e) {
			logger.warn("Failed to load ticket data.");
			e.printStackTrace();
		}
		index = new AtomicLong(tickets.size());
		logger.info("Loaded ticket data with {} entries.", tickets.size());
	}

	@Override
	public Map<Long, Ticket> getData() {
		return tickets;
	}
}
