package org.example.groovy.org.example

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.example.controller.UserController
import org.example.dto.ErrorDto
import org.example.dto.UserDto
import org.example.exception.EntityNotFoundException
import org.example.facade.BookingFacade
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

import static org.example.util.TestUtils.DEFAULT_USER_EMAIL
import static org.example.util.TestUtils.DEFAULT_USER_NAME
import static org.example.util.TestUtils.ID_ONE
import static org.example.util.TestUtils.NOT_EXISTING_ID
import static org.example.util.TestUtils.SLASH
import static org.example.util.TestUtils.createDefaultUserDto
import static org.example.util.TestUtils.createUserDtoWithoutId
import static org.mockito.AdditionalAnswers.returnsSecondArg
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerSpecification extends Specification {

    final CONTROLLER_PATH = "/users"

    @MockBean
    private BookingFacade bookingFacade

    @MockBean
    private ConnectionFactory mockFactory

    @Autowired
    private ObjectMapper mapper

    @Autowired
    private MockMvc mockMvc

    def "Test create user with valid data"() {
        setup:
        def userDtoWithId = createDefaultUserDto()
        when(bookingFacade.createUser(any(UserDto.class))).thenReturn(userDtoWithId)

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createUserDtoWithoutId())))
                .andReturn()

        then:
        result.response.status == HttpStatus.CREATED.value()

        and:
        with (mapper.readValue(result.response.contentAsString, UserDto)) {
            it.id == userDtoWithId.id
            it.name == userDtoWithId.name
            it.email == userDtoWithId.email
        }
    }

    def "Test create user with preexisting email"() {
        setup:
        def userDto = createUserDtoWithoutId()
        when(bookingFacade.createUser(any(UserDto.class))).thenThrow(new IllegalArgumentException("User email must be unique"))

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "User email must be unique"
        }
    }

    def "Test create user with id not null"() {
        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createDefaultUserDto())))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createUser.user.id: must be null"
        }
    }

    def "Test create user with blank username"() {
        setup:
        def userDto = createUserDtoWithoutId()
        userDto.name = ""

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "createUser.user.name: must not be blank"
        }
    }

    def "Test create user with invalid email: #email"() {
        setup:
        def userDto = createUserDtoWithoutId()
        userDto.email = email

        when:
        def result = mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == errorMessage
        }

        where:
               email        |               errorMessage
               null         |  "createUser.user.email: must not be blank"
                ""          |  "createUser.user.email: must not be blank"
           "not an email"   |   "must be a well-formed email address"
    }

    def "Test get user by existing id"() {
        setup:
        def userDto = createDefaultUserDto()
        when(bookingFacade.getUserById(ID_ONE)).thenReturn(userDto)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + SLASH + ID_ONE)).andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, UserDto)) {
            it.id == userDto.id
            it.name == userDto.name
            it.email == userDto.email
        }
    }

    def "Test get user by not existing id"() {
        setup:
        when(bookingFacade.getUserById(NOT_EXISTING_ID))
                        .thenThrow(new EntityNotFoundException("Entity not found by id: " + NOT_EXISTING_ID))

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)).andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "Entity not found by id: $NOT_EXISTING_ID"
        }
    }

    def "Test get user by existing email"() {
        setup:
        def userDto = createDefaultUserDto()
        when(bookingFacade.getUserByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDto)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + "/byEmail")
                                    .param("email", DEFAULT_USER_EMAIL))
                                    .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, UserDto)) {
            it.id == userDto.id
            it.name == userDto.name
            it.email == userDto.email
        }
    }

    def "Test get user by not existing email"() {
        setup:
        when(bookingFacade.getUserByEmail(DEFAULT_USER_EMAIL))
                .thenThrow(new EntityNotFoundException("User not found by email: " + DEFAULT_USER_EMAIL))

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + "/byEmail")
                                    .param("email", DEFAULT_USER_EMAIL))
                                    .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "User not found by email: $DEFAULT_USER_EMAIL"
        }
    }

    def "Test get user by name with existing name"() {
        setup:
        def userDtoList = List.of(createDefaultUserDto())
        def userDtoPage = new PageImpl<>(userDtoList, Pageable.unpaged(), userDtoList.size())
        when(bookingFacade.getUsersByName(eq(DEFAULT_USER_NAME), any(Pageable.class))).thenReturn(userDtoPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + "/byName")
                .param("name", DEFAULT_USER_NAME))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<UserDto>>() {})) {
            it.content.size() == 1
            it.first().name == DEFAULT_USER_NAME
        }
    }

    def "Test get user by name with not existing name"() {
        setup:
        def emptyPage = new PageImpl<UserDto>(List.of(), Pageable.unpaged(), 0)
        when(bookingFacade.getUsersByName(eq(DEFAULT_USER_NAME), any(Pageable.class))).thenReturn(emptyPage)

        when:
        def result = mockMvc.perform(get(CONTROLLER_PATH + "/byName")
                .param("name", DEFAULT_USER_NAME))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, new TypeReference<TestPageImpl<UserDto>>() {})) {
            it.content.size() == 0
        }
    }

    def "Test update user with valid data"() {
        setup:
        def userDto = createDefaultUserDto()
        when(bookingFacade.updateUser(ID_ONE, userDto)).then(returnsSecondArg())

        when:
        def result = mockMvc.perform(put(CONTROLLER_PATH + SLASH + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        with (mapper.readValue(result.response.contentAsString, UserDto)) {
            it.id == userDto.id
            it.name == userDto.name
            it.email == userDto.email
        }
    }

    def "Test update user with not existing id"() {
        setup:
        def userDto = createDefaultUserDto()
        when(bookingFacade.updateUser(NOT_EXISTING_ID, userDto))
                .thenThrow(new EntityNotFoundException("User not found by id: $NOT_EXISTING_ID"))

        when:
        def result = mockMvc.perform(put(CONTROLLER_PATH + SLASH + NOT_EXISTING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto)))
                .andReturn()

        then:
        result.response.status == HttpStatus.NOT_FOUND.value()

        and:
        with (mapper.readValue(result.response.contentAsString, ErrorDto)) {
            it.message == "User not found by id: $NOT_EXISTING_ID"
        }
    }

    def "Test delete user with existing id"() {
        when:
        def result = mockMvc.perform(delete(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ID_ONE)))
                .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()
    }
}
