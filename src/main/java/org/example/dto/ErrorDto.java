package org.example.dto;

import org.springframework.http.HttpStatus;

public class ErrorDto {

	private HttpStatus status;

	private int statusCode;

	private String message;

	public ErrorDto() {
	}

	public ErrorDto(HttpStatus status, int statusCode, String error) {
		this.status = status;
		this.statusCode = statusCode;
		this.message = error;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
