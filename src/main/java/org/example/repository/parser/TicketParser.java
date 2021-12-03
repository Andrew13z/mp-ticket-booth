package org.example.repository.parser;

import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.springframework.stereotype.Component;

@Component
public class TicketParser extends AbstractParser<Ticket> {

	@Override
	public Ticket parse(String content) {
		var columns = content.split(",");
		return new TicketBuilder().setId(Long.parseLong(columns[0]))
				.setEventId(Long.parseLong(columns[1]))
				.setUserId(Long.parseLong(columns[2]))
				.setCategory(Ticket.Category.valueOf(columns[3]))
				.setPlace(Integer.parseInt(columns[4]))
				.createTicket();
	}

	@Override
	public long getId(Ticket model) {
		return model.getId();
	}

	@Override
	public Class<Ticket> getType() {
		return Ticket.class;
	}
}
