package org.example.exception.handler;

import org.example.dto.ErrorDto;
import org.example.exception.AccountBalanceException;
import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.example.exception.UnmarshallingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class ApplicationExceptionHandler {

	private final Map<Class<?>, Function<Exception, ResponseEntity<ErrorDto>>> handlerMap=
						Map.of(EntityNotFoundException.class, this::handleEntityNotFoundException,
								PdfGenerationException.class, this::handlePdfGenerationException,
								UnmarshallingException.class, this::handleUnmarshallingException,
								IllegalArgumentException.class, this::handleIllegalArgumentException,
								AccountBalanceException.class, this::handleAccountBalanceException);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleException(Exception ex) {
		return handlerMap.getOrDefault(ex.getClass(), this::handleDefaultException).apply(ex);
	}

	/**
	 * Handles EntityNotFoundException
	 *
	 * @param exception thrown EntityNotFoundException
	 * @return ResponseEntity with ErrorDto
	 */
	public ResponseEntity<ErrorDto> handleEntityNotFoundException(Exception exception) {
		return ResponseEntity.status(NOT_FOUND)
							.contentType(APPLICATION_JSON)
							.body(createErrorDto(NOT_FOUND, exception.getMessage()));
	}

	/**
	 * Handles EntityNotFoundException
	 *
	 * @param exception thrown EntityNotFoundException
	 * @return ResponseEntity with ErrorDto
	 */
	private ResponseEntity<ErrorDto> handlePdfGenerationException(Exception exception) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
							.contentType(APPLICATION_JSON)
							.body(createErrorDto(INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

	/**
	 * Handles UnmarshallingException
	 *
	 * @param exception thrown UnmarshallingException
	 * @return ResponseEntity with ErrorDto
	 */
	private ResponseEntity<ErrorDto> handleUnmarshallingException(Exception exception) {
		return createResponseEntityWithBadRequestStatus(exception.getMessage());
	}

	/**
	 * Handles IllegalArgumentException
	 *
	 * @param exception thrown IllegalArgumentException
	 * @return ResponseEntity with ErrorDto
	 */
	private ResponseEntity<ErrorDto> handleIllegalArgumentException(Exception exception) {
		return createResponseEntityWithBadRequestStatus(exception.getMessage());
	}

	/**
	 * Handles AccountBalanceException
	 *
	 * @param exception thrown AccountBalanceException
	 * @return ResponseEntity with ErrorDto
	 */
	private ResponseEntity<ErrorDto> handleAccountBalanceException(Exception exception) {
		return createResponseEntityWithBadRequestStatus(exception.getMessage());
	}


	/**
	 * Handles Exception
	 *
	 * @param exception thrown Exception
	 * @return ResponseEntity with ErrorDto
	 */
	private ResponseEntity<ErrorDto> handleDefaultException(Exception exception) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.contentType(APPLICATION_JSON)
				.body(createErrorDto(INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

	/**
	 * Creates an instance of ResponseEntity with status BAD_REQUEST
	 * and ErrorDto that has BAD_REQUEST status and the provided message
	 *
	 * @param message message of the exception
	 * @return ResponseEntity<ErrorDto>
	 */
	private ResponseEntity<ErrorDto> createResponseEntityWithBadRequestStatus(String message) {
		return ResponseEntity.status(BAD_REQUEST)
						.contentType(APPLICATION_JSON)
						.body(createErrorDto(BAD_REQUEST, message));
	}
	/**
	 * Creates an instance of ErrorDto
	 *
	 * @param status HttpStatus of the exception
	 * @param message message of the exception
	 * @return ErrorDto
	 */
	private ErrorDto createErrorDto(HttpStatus status, String message) {
		return new ErrorDto(status, status.value(), message);
	}

}
