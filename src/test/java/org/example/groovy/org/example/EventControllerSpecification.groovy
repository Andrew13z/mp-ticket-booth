package org.example.groovy.org.example

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.example.controller.EventController
import org.example.dto.ErrorDto
import org.example.dto.EventDto
import org.example.exception.EntityNotFoundException
import org.example.service.EventService
import org.example.util.TestPageImpl
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.jms.ConnectionFactory
import java.time.LocalDate

import static org.example.util.TestUtils.DEFAULT_EVENT_DATE
import static org.example.util.TestUtils.DEFAULT_EVENT_TITLE
import static org.example.util.TestUtils.ID_ONE
import static org.example.util.TestUtils.NOT_EXISTING_EVENT_DATE
import static org.example.util.TestUtils.NOT_EXISTING_EVENT_TITLE
import static org.example.util.TestUtils.NOT_EXISTING_ID
import static org.example.util.TestUtils.SLASH
import static org.example.util.TestUtils.createDefaultEventDto
import static org.example.util.TestUtils.createEventDtoWithoutId
import static org.mockito.AdditionalAnswers.returnsSecondArg
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@WebMvcTest(EventController.class)
@ExtendWith(MockitoExtension.class)
class EventControllerSpecification extends Specification {

    final CONTROLLER_PATH = "/events"

    @MockBean
    private EventService eventService

    @MockBean
    private ConnectionFactory mockFactory

    @Autowired
    private ObjectMapper mapper

    @Autowired
    private MockMvc mockMvc

    def "Test create event with valid data"() {
        setup:
        def eventDtoWithId = createDefaultEventDto()
        when(eventService.createEvent(any(EventDto.class))).thenReturn(eventDtoWithId)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createEventDtoWithoutId())))
                .andReturn()

        then:
        result.response.status == HttpStatus.CREATED.value()

        and:
        with (mapper.readValue(result.response.contentAsString, EventDto)) {
            it.id == eventDtoWithId.id
            it.title == eventDtoWithId.title
            it.date == eventDtoWithId.date
        }
    }

    def "Test create event with id not null"() {
        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDefaultEventDto())))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createEvent.event.id: must be null"
        }
    }

    def "Test create event with blank event title"() {
        setup:
        def eventDto = createEventDtoWithoutId()
        eventDto.title = ""

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createEvent.event.title: must not be blank"
        }
    }

    def "Test create event with invalid date: #date"() {
        setup:
        def eventDto = createEventDtoWithoutId()
        eventDto.date = date

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == errorMessage
        }

        where:
                        date            |               errorMessage
                        null            |  "createEvent.event.date: must not be null"
          LocalDate.now().minusDays(1)  |  "The event date must be today or later"
    }

    def "Test create event with negative ticket price"() {
        setup:
        def eventDto = createEventDtoWithoutId()
        eventDto.ticketPrice = BigDecimal.valueOf(-1)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "must be greater than or equal to 0"
        }
    }

    def "Test get event by existing id"() {
        setup:
        def eventDto = createDefaultEventDto()
        when(eventService.getEventById(ID_ONE)).thenReturn(eventDto)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + SLASH + ID_ONE)).andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, EventDto)) {
            it.id == eventDto.id
            it.title == eventDto.title
            it.date == eventDto.date
        }
    }

    def "Test get event by not existing id"() {
        setup:
        when(eventService.getEventById(ID_ONE)).thenThrow(new EntityNotFoundException("Entity not found by id: $ID_ONE"))

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + SLASH + ID_ONE)).andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Entity not found by id: $ID_ONE"
        }
    }

    def "Test get event by title with existing title"() {
        setup:
        def eventDtoList = List.of(createDefaultEventDto())
        def eventDtoPage = new PageImpl<>(eventDtoList, Pageable.unpaged(), eventDtoList.size())
        when(eventService.getEventsByTitle(eq(DEFAULT_EVENT_TITLE), any(Pageable.class))).thenReturn(eventDtoPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                                        .param("title", DEFAULT_EVENT_TITLE))
                                        .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<EventDto>>() {})) {
            it.content.size() == 1
            it.first().title == DEFAULT_EVENT_TITLE
        }
    }

    def "Test get event by title with not existing title"() {
        setup:
        def emptyPage = new PageImpl<EventDto>(List.of(), Pageable.unpaged(), 0)
        when(eventService.getEventsByTitle(eq(NOT_EXISTING_EVENT_TITLE), any(Pageable.class))).thenReturn(emptyPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("title", NOT_EXISTING_EVENT_TITLE))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<EventDto>>() {})) {
            it.content.size() == 0
        }
    }

    def "Test get event by date with existing date"() {
        setup:
        def eventDtoList = List.of(createDefaultEventDto())
        def eventDtoPage = new PageImpl<>(eventDtoList, Pageable.unpaged(), eventDtoList.size())
        when(eventService.getEventsForDay(eq(DEFAULT_EVENT_DATE), any(Pageable.class))).thenReturn(eventDtoPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("date", DEFAULT_EVENT_DATE.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<EventDto>>() {})) {
            it.content.size() == 1
            it.first().date == DEFAULT_EVENT_DATE
        }
    }

    def "Test get event by date with not existing date"() {
        setup:
        def emptyPage = new PageImpl<EventDto>(List.of(), Pageable.unpaged(), 0)
        when(eventService.getEventsForDay(eq(NOT_EXISTING_EVENT_DATE), any(Pageable.class))).thenReturn(emptyPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("date", NOT_EXISTING_EVENT_DATE.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<EventDto>>() {})) {
            it.content.size() == 0
        }
    }

    def "Test update event with valid data"() {
        setup:
        def eventDto = createDefaultEventDto()
        when(eventService.updateEvent(ID_ONE, eventDto)).then(returnsSecondArg())

        when:
        def result = mockMvc.perform(put(CONTROLLER_PATH + SLASH + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, EventDto)) {
            it.id == eventDto.id
            it.title == eventDto.title
            it.date == eventDto.date
        }
    }

    def "Test update event with not existing id"() {
        setup:
        def eventDto = createDefaultEventDto()
        when(eventService.updateEvent(NOT_EXISTING_ID, eventDto))
                .thenThrow(new EntityNotFoundException("Entity not found by id: $NOT_EXISTING_ID"))

        when:
        def result = mockMvc.perform(put(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Entity not found by id: $NOT_EXISTING_ID"
        }
    }

    def "Test delete event with existing id"() {
        when:
        def result = mockMvc.perform(delete(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ID_ONE)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()
    }
}
