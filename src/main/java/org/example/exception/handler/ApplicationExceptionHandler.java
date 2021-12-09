package org.example.exception.handler;

import org.example.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public String handleException(){
		return "error";
	}

}
