package org.example.exception.handler;

import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApplicationExceptionHandler {

	private static final String ERROR_VIEW_NAME = "error";

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFoundException(Exception e){
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}

	@ExceptionHandler(PdfGenerationException.class)
	public ModelAndView handlePdfGenerationException(Exception e){
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}

}
