package org.example.converter;

import org.example.exception.UnmarshallingException;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Converter for unmarshalling xml file with ticket data to list of tickets
 * @author Andrii Krokhta
 */
@Component
public class XmlConverter {

	private static final Logger logger = LoggerFactory.getLogger(XmlConverter.class);

	@Autowired
	private Unmarshaller unmarshaller;

	@Value("${tickets.source}")
	private Resource ticketsFile;

	@Value("${users.source}")
	private Resource usersFile;

	@Value("${events.source}")
	private Resource eventsFile;

	/**
	 * Converter the xml file with ticket data to list of tickets.
	 * @return List of tickets.
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> parseTicketXmlToObject() throws IOException {
		try (FileInputStream inputStream = new FileInputStream(ticketsFile.getFile())) {
			return (List<Ticket>) unmarshaller.unmarshal(new StreamSource(inputStream));
		}
	}

	/**
	 * Converter the xml file with ticket data to list of tickets.
	 * @return List of tickets.
	 */
	@SuppressWarnings("unchecked")
	public List<Ticket> parseTicketXmlToObject(InputStream stream) throws IOException {
		var unmarshalledData = (ArrayList<Ticket>) unmarshaller.unmarshal(new StreamSource(stream));
		if (!unmarshalledData.isEmpty() && unmarshalledData.get(0) instanceof Ticket) {
			return unmarshalledData;
		} else {
			logger.warn("Failed to unmarshal ticket list from te provided file.");
			throw new UnmarshallingException("Invalid data in the provided file.");
		}
	}

	/**
	 * Converter the xml file with suer data to list of users.
	 * @return List of tickets.
	 */
	@SuppressWarnings("unchecked")
	public List<User> parseUserXmlToObject() throws IOException {
		try (FileInputStream inputStream = new FileInputStream(usersFile.getFile())) {
			return (List<User>) unmarshaller.unmarshal(new StreamSource(inputStream));
		}
	}

	/**
	 * Converter the xml file with ticket data to list of tickets.
	 * @return List of tickets.
	 */
	@SuppressWarnings("unchecked")
	public List<Event> parseEventXmlToObject() throws IOException {
		try (FileInputStream inputStream = new FileInputStream(eventsFile.getFile())) {
			return (List<Event>) unmarshaller.unmarshal(new StreamSource(inputStream));
		}
	}


}
