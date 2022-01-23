package org.example.dto;

import org.springframework.http.HttpStatus;

public class ErrorDto {

	private final HttpStatus status;

	private final int statusCode;

	private final String message;

	public ErrorDto(HttpStatus status, int statusCode, String error) {
		this.status = status;
		this.statusCode = statusCode;
		this.message = error;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

}
