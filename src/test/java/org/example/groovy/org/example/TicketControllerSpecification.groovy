package org.example.groovy.org.example

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.example.controller.TicketController
import org.example.dto.ErrorDto
import org.example.dto.TicketDto
import org.example.exception.AccountBalanceException
import org.example.exception.EntityNotFoundException
import org.example.service.TicketService
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

import static org.example.util.TestUtils.DEFAULT_TICKET_CATEGORY
import static org.example.util.TestUtils.DEFAULT_TICKET_PLACE
import static org.example.util.TestUtils.ID_ONE
import static org.example.util.TestUtils.NOT_EXISTING_ID
import static org.example.util.TestUtils.createDefaultTicketDto
import static org.example.util.TestUtils.createTicketDtoForTicketCreateOperation
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest(TicketController.class)
@ExtendWith(MockitoExtension.class)
class TicketControllerSpecification extends Specification {

    final CONTROLLER_PATH = "/tickets"

    @MockBean
    private TicketService ticketService

    @MockBean
    private ConnectionFactory mockFactory

    @Autowired
    private ObjectMapper mapper

    @Autowired
    private MockMvc mockMvc

    def "Test create ticket with valid data"() {
        setup:
        def ticketDto = createDefaultTicketDto()
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenReturn(ticketDto)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTicketDtoForTicketCreateOperation())))
                .andReturn()

        then:
        result.response.status == HttpStatus.CREATED.value()

        and:
        with(mapper.readValue(result.response.contentAsString, TicketDto)) {
            it.id == ticketDto.id
            it.user.id == ticketDto.user.id
            it.event.id == ticketDto.event.id
            it.category == ticketDto.category
            it.place == ticketDto.place
        }
    }

    def "Test create ticket with id not null"() {
        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDefaultTicketDto())))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createTicket.ticket.id: must be null"
        }
    }

    def "Test create ticket without user id"() {
        setup:
        def ticketDto = createTicketDtoForTicketCreateOperation()
        ticketDto.user.id = null
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenReturn(ticketDto)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ticketDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createTicket.ticket.user.id: must not be null"
        }
    }

    def "Test create ticket with not existing user"() {
        setup:
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenThrow(new EntityNotFoundException("Account not found by id: $ID_ONE"))

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTicketDtoForTicketCreateOperation())))
                .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Account not found by id: $ID_ONE"
        }
    }

    def "Test create ticket without event id"() {
        setup:
        def ticketDto = createTicketDtoForTicketCreateOperation()
        ticketDto.event.id = null
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenReturn(ticketDto)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ticketDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createTicket.ticket.event.id: must not be null"
        }
    }

    def "Test create ticket with not existing event"() {
        setup:
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenThrow(new EntityNotFoundException("Event not found by id: $ID_ONE"))

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTicketDtoForTicketCreateOperation())))
                .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Event not found by id: $ID_ONE"
        }
    }

    def "Test create ticket with insufficient account balance"() {
        setup:
        when(ticketService.bookTicket(ID_ONE, ID_ONE, DEFAULT_TICKET_CATEGORY, DEFAULT_TICKET_PLACE))
                .thenThrow(new AccountBalanceException("Account has insufficient funds."))

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTicketDtoForTicketCreateOperation())))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Account has insufficient funds."
        }
    }

    def "Test get ticket by user with existing user"() {
        setup:
        def ticketDtoList = List.of(createDefaultTicketDto())
        def ticketDtoPage = new PageImpl<>(ticketDtoList, Pageable.unpaged(), ticketDtoList.size())
        when(ticketService.getBookedTicketsByUserId(eq(ID_ONE), any(Pageable.class))).thenReturn(ticketDtoPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("userId", ID_ONE.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with(mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<TicketDto>>() {})) {
            it.content.size() == 1
            it.first().id == ID_ONE
            it.first().user.id == ID_ONE
        }
    }

    def "Test get ticket by user with not existing user"() {
        setup:
        def emptyPage = new PageImpl<TicketDto>(List.of(), Pageable.unpaged(), 0)
        when(ticketService.getBookedTicketsByUserId(eq(NOT_EXISTING_ID), any(Pageable.class))).thenReturn(emptyPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("userId", NOT_EXISTING_ID.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with(mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<TicketDto>>() {})) {
            it.content.size() == 0
        }
    }

    def "Test get ticket by event with existing event"() {
        setup:
        def ticketDtoList = List.of(createDefaultTicketDto())
        def ticketDtoPage = new PageImpl<>(ticketDtoList, Pageable.unpaged(), ticketDtoList.size())
        when(ticketService.getBookedTicketsByEventId(eq(ID_ONE), any(Pageable.class))).thenReturn(ticketDtoPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("eventId", ID_ONE.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with(mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<TicketDto>>() {})) {
            it.content.size() == 1
            it.first().id == ID_ONE
            it.first().event.id == ID_ONE
        }
    }

    def "Test get ticket by event with not existing event"() {
        setup:
        def emptyPage = new PageImpl<TicketDto>(List.of(), Pageable.unpaged(), 0)
        when(ticketService.getBookedTicketsByEventId(eq(NOT_EXISTING_ID), any(Pageable.class))).thenReturn(emptyPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH)
                .param("eventId", NOT_EXISTING_ID.toString()))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with(mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<TicketDto>>() {})) {
            it.content.size() == 0
        }
    }

    def "Test delete ticket with existing id"() {
        when:
        def result = mockMvc.perform(delete(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ID_ONE)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()
    }

}
