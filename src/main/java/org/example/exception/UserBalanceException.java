package org.example.exception;

public class UserBalanceException extends RuntimeException{

	public UserBalanceException(String message) {
		super(message);
	}
}
