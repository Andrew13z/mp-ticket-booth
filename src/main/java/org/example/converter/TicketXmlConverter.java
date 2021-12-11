package org.example.converter;

import org.example.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class TicketXmlConverter {

	@Autowired
	private Unmarshaller unmarshaller;

	@Value("${tickets.source.xml}")
	private Resource ticketsFile;

	@SuppressWarnings("unchecked")
	public List<Ticket> xmlToObject() throws IOException {
		try (FileInputStream inputStream = new FileInputStream(ticketsFile.getFile())) {
			return (List<Ticket>) unmarshaller.unmarshal(new StreamSource(inputStream));
		}
	}
}
