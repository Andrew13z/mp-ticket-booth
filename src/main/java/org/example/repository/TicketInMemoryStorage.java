package org.example.repository;

import org.example.model.Ticket;
import org.example.repository.parser.AbstractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TicketInMemoryStorage extends InMemoryStorage<Ticket> {

	private static final Logger logger = LoggerFactory.getLogger(TicketInMemoryStorage.class);

	@Value("${tickets.source}")
	private String ticketsFileName;

	private Map<Long, Ticket> tickets;

	@Autowired
	public TicketInMemoryStorage(AbstractParser<Ticket> parser) {
		super(parser);
	}

	@PostConstruct
	private void postConstruct() {
		tickets = parser.loadData(ticketsFileName);
		index = new AtomicLong(tickets.size());
		logger.info("Loaded ticket data with {} entries.", tickets.size());
	}

	@Override
	public Map<Long, Ticket> getData() {
		return tickets;
	}
}
