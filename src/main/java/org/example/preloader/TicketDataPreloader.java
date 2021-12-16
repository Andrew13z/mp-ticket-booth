package org.example.preloader;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.converter.XmlMarshaller;
import org.example.model.Ticket;
import org.example.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data preloader for Ticket.
 */
@Component
public class TicketDataPreloader implements DataPreloader<Ticket>{

	private static final Logger logger = LoggerFactory.getLogger(TicketDataPreloader.class);

	@Value("${tickets.source}")
	private Resource ticketsFile;

	private final XmlMarshaller xmlMarshaller;
	private final TicketService ticketService;

	@Autowired
	public TicketDataPreloader(XmlMarshaller xmlMarshaller, TicketService ticketService) {
		this.xmlMarshaller = xmlMarshaller;
		this.ticketService = ticketService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Ticket> preloadData() {
		List<Ticket> tickets = new ArrayList<>();
		try {
			tickets = xmlMarshaller.parse(ticketsFile, new TypeReference<>(){});
		} catch (IOException e) {
			logger.warn("Failed to load ticket data: {}", e.getMessage());
			e.printStackTrace();
		}
		tickets.forEach(ticket -> ticketService.bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getCategory(), ticket.getPlace()));
		logger.info("Loaded ticket data with {} entries.", tickets.size());
		return tickets;
	}
}
