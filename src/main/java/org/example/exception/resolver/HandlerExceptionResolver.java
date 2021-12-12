package org.example.exception.resolver;

import org.example.exception.EntityNotFoundException;
import org.example.exception.PdfGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(HandlerExceptionResolver.class);

	private static final String ERROR_VIEW_NAME = "error";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof EntityNotFoundException) {
			return handleEntityNotFoundException((EntityNotFoundException) ex);
		} else if (ex instanceof PdfGenerationException) {
			return handlePdfGenerationException((PdfGenerationException) ex);
		} else {
			return handleException(ex);
		}
	}

	private ModelAndView handleEntityNotFoundException(EntityNotFoundException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.NOT_FOUND);
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	private ModelAndView handlePdfGenerationException(PdfGenerationException ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	private ModelAndView handleException(Exception ex) {
		var modelAndView = new ModelAndView();
		modelAndView.setViewName(ERROR_VIEW_NAME);
		modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

}
