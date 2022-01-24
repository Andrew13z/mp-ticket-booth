package org.example.exception;

public class UnmarshallingException extends RuntimeException{

	public UnmarshallingException(String message, Throwable cause){
		super(message, cause);
	}
}
