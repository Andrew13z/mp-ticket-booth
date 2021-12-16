package org.example.exception.handler;

import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.example.exception.UnmarshallingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ApplicationExceptionHandler {

	private static final String ERROR_VIEW_NAME = "error.html";
	private static final String MESSAGE = "message";

	/**
	 * Handles EntityNotFoundException
	 *
	 * @param ex thrown EntityNotFoundException
	 * @return model with view name, response status, and message
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.NOT_FOUND);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles PdfGenerationException
	 *
	 * @param ex thrown PdfGenerationException
	 * @return model with view name, response status, and message
	 */
	@ExceptionHandler(PdfGenerationException.class)
	private ModelAndView handlePdfGenerationException(PdfGenerationException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles UnmarshallingException
	 *
	 * @param ex thrown UnmarshallingException
	 * @return model with view name, response status, and message
	 */
	@ExceptionHandler(UnmarshallingException.class)
	private ModelAndView handleUnmarshallingException(UnmarshallingException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.BAD_REQUEST);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles IllegalArgumentException
	 *
	 * @param ex thrown IllegalArgumentException
	 * @return model with view name, response status, and message
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	private ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.BAD_REQUEST);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles Exception
	 *
	 * @param ex thrown Exception
	 * @return model with view name, response status, and message
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleGenericException(Exception ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}
}
