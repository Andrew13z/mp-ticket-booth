package org.example.exception.resolver;

import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom exception resolver
 * @author Andrii Krokhta
 */
@Component
public class CustomExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final String ERROR_VIEW_NAME = "error";
	private static final String MESSAGE = "message";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof EntityNotFoundException) {
			return handleEntityNotFoundException((EntityNotFoundException) ex);
		} else if (ex instanceof PdfGenerationException) {
			return handlePdfGenerationException((PdfGenerationException) ex);
		} else if (ex instanceof IllegalArgumentException){
			return handleIllegalArgumentException((IllegalArgumentException) ex);
		} else{
			return handleException(ex);
		}
	}

	/**
	 * Handles EntityNotFoundException
	 * @param ex thrown EntityNotFoundException
	 * @return model with view name, response status, and message
	 */
	private ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.NOT_FOUND);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles PdfGenerationException
	 * @param ex thrown PdfGenerationException
	 * @return model with view name, response status, and message
	 */
	private ModelAndView handlePdfGenerationException(PdfGenerationException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles IllegalArgumentException
	 * @param ex thrown IllegalArgumentException
	 * @return model with view name, response status, and message
	 */
	private ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.BAD_REQUEST);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

	/**
	 * Handles generic exceptions
	 * @param ex thrown Exception
	 * @return model with view name, response status, and message
	 */
	private ModelAndView handleException(Exception ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject(MESSAGE, ex.getMessage());
		return modelAndView;
	}

}
