package org.example.repository.util;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketBuilder;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	private static final String COMMA = ",";

	private static String ticketsFile;

	private static String eventsFile;

	private static String usersFile;

	@Value("${tickets.source}")
	private String ticketsFileTemp;

	@Value("${events.source}")
	private String eventsFileTemp;

	@Value("${users.source}")
	private String usersFileTemp;

	@PostConstruct
	private void init() {
		ticketsFile = ticketsFileTemp;
		eventsFile = eventsFileTemp;
		usersFile = usersFileTemp;
	}

	private DataInitializer() {
		//private constructor
	}

	public static Map<Long, User> initializeUsers() {
		Map<Long, User> users = null;
		try (Stream<String> userInfo = Files.lines(Path.of(usersFile))) {
			users = userInfo.map(DataInitializer::getUser)
					.collect(Collectors.toMap(User::getId, Function.identity()));
		} catch (IOException ioException) {
			logger.warn("Failed to initialize user data.");
			ioException.printStackTrace();
		}
		return users != null ? users : new HashMap<>();
	}

	private static User getUser(String info) {
		var columns = info.split(COMMA);
		return new User(Long.parseLong(columns[0]), columns[1], columns[2]);
	}

	public static Map<Long, Event> initializeEvents() {
		Map<Long, Event> events = null;
		try (Stream<String> eventInfo = Files.lines(Path.of(eventsFile))) {
			events = eventInfo.map(DataInitializer::getEvent)
					.collect(Collectors.toMap(Event::getId, Function.identity()));
		} catch (IOException ioException) {
			logger.warn("Failed to initialize event data.");
			ioException.printStackTrace();
		}
		return events != null ? events : new HashMap<>();
	}

	private static Event getEvent(String info) {
		var columns = info.split(COMMA);
		return new Event(Long.parseLong(columns[0]), columns[1], LocalDate.parse(columns[2]));
	}

	public static Map<Long, Ticket> initializeTickets() {
		Map<Long, Ticket> tickets = null;
		try (Stream<String> ticketInfo = Files.lines(Path.of(ticketsFile))) {
			tickets = ticketInfo.map(DataInitializer::getTicket)
					.collect(Collectors.toMap(Ticket::getId, Function.identity()));
		} catch (IOException ioException) {
			logger.warn("Failed to initialize ticket data.");
			ioException.printStackTrace();
		}
		return tickets != null ? tickets : new HashMap<>();
	}

	private static Ticket getTicket(String info) {
		var columns = info.split(COMMA);
		return new TicketBuilder().setId(Long.parseLong(columns[0]))
				.setEventId(Long.parseLong(columns[1]))
				.setUserId(Long.parseLong(columns[2]))
				.setCategory(Ticket.Category.valueOf(columns[3]))
				.setPlace(Integer.parseInt(columns[4]))
				.createTicket();
	}
}
