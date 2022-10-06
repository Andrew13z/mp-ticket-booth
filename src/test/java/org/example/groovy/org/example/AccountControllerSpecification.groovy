package org.example.groovy.org.example

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.controller.AccountController
import org.example.dto.AccountDto
import org.example.dto.ErrorDto
import org.example.exception.EntityNotFoundException
import org.example.service.AccountService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.jms.ConnectionFactory

import static org.example.util.TestUtils.ID_ONE
import static org.example.util.TestUtils.SLASH
import static org.mockito.Mockito.when
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch

@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerSpecification extends Specification {

    final CONTROLLER_PATH = "/accounts"

    @MockBean
    private AccountService accountService

    @MockBean
    private ConnectionFactory mockFactory

    @Autowired
    private ObjectMapper mapper

    @Autowired
    private MockMvc mockMvc

    def "Test refill account by id with valid data"() {
        setup:
        when(accountService.refillAccount(1L, BigDecimal.TEN))
                .thenReturn(new AccountDto(1L, BigDecimal.TEN))
        when:
        def result = mockMvc.perform(patch(CONTROLLER_PATH + SLASH + ID_ONE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(BigDecimal.TEN)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with(mapper.readValue(result.response.contentAsString, AccountDto)) {
            it.id == ID_ONE
            it.balance == BigDecimal.TEN
        }
    }

    def "Test refill account by id with not existing user"() {
        setup:
        def id = 1L
        when(accountService.refillAccount(id, BigDecimal.TEN))
                .thenThrow(new EntityNotFoundException("Entity not found by id: $id"))
        when:
        def result = mockMvc.perform(patch(CONTROLLER_PATH + SLASH + ID_ONE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(BigDecimal.TEN)))
                .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Entity not found by id: $ID_ONE"
        }
    }

    def "Test refill account by id with negative refill sum"() {
        when:
        def result = mockMvc.perform(patch(CONTROLLER_PATH + SLASH + ID_ONE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(BigDecimal.valueOf(-1))))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "refillAccount.refillSum: must be greater than or equal to 0"
        }
    }
}
