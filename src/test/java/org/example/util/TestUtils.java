package org.example.util;

import org.example.dto.EventDto;
import org.example.dto.TicketDto;
import org.example.dto.UserDto;
import org.example.enums.Category;
import org.example.model.TicketBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestUtils {

	public static final Long ID_ONE = 1L;
	public static final Long NOT_EXISTING_ID = 100L;

	public static final String DEFAULT_EVENT_TITLE = "Leading Edge Events";
	public static final LocalDate DEFAULT_EVENT_DATE = LocalDate.now().plusMonths(6);
	public static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.valueOf(25.75);

	public static final String NOT_EXISTING_EVENT_TITLE = "Not a title";
	public static final LocalDate NOT_EXISTING_EVENT_DATE = LocalDate.now().minusYears(100);

	public static final String NEW_EVENT_TITLE = "Beyond All Limits";
	public static final LocalDate NEW_EVENT_DATE = LocalDate.now().plusYears(2);
	public static final BigDecimal NEW_TICKET_PRICE = BigDecimal.valueOf(75.25);

	public static final String DEFAULT_USER_NAME = "Daniel Jaxon";
	public static final String DEFAULT_USER_EMAIL = "dan.jax@gamil.com";

	public static final String PREEXISTING_USER_NAME = "Danny Dempsey";
	public static final String PREEXISTING_USER_EMAIL = "Danny_Dempsey454@mafthy.com";
	public static final String PREEXISTING_USER_ACCOUNT_BALANCE = "Danny_Dempsey454@mafthy.com";

	public static final String NEW_USER_NAME = "Dolores Croft";
	public static final String NEW_USER_EMAIL = "dan.jax@gamil.com";

	public static final Category DEFAULT_TICKET_CATEGORY = Category.STANDARD;
	public static final int DEFAULT_TICKET_PLACE = 1;


	public static final String SLASH = "/";

	public static EventDto createDefaultEventDto(){
		return new EventDto(ID_ONE, DEFAULT_EVENT_TITLE, DEFAULT_EVENT_DATE, DEFAULT_TICKET_PRICE);
	}

	public static EventDto createEventDtoWithoutId(){
		return new EventDto(null, DEFAULT_EVENT_TITLE, DEFAULT_EVENT_DATE, DEFAULT_TICKET_PRICE);
	}

	public static UserDto createDefaultUserDto() {
		return new UserDto(ID_ONE, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL);
	}

	public static UserDto createUserDtoWithoutId() {
		return new UserDto(null, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL);
	}

	public static TicketDto createTicketDtoFotTicketCreateOperation(){
		return new TicketDto(null,
				new UserDto(ID_ONE, null, null),
				new EventDto(ID_ONE, null, null, BigDecimal.ZERO),
				DEFAULT_TICKET_CATEGORY,
				DEFAULT_TICKET_PLACE);
	}
}
