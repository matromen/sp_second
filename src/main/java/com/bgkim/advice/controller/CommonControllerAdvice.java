package com.bgkim.advice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.log4j.Log4j;

@ControllerAdvice
@Log4j

public class CommonControllerAdvice {
	/*
	 * Exception Advice 500
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(Exception ex, Model model) {
		log.error("Exception ......................" + ex);

		model.addAttribute("ex", ex);
		
		return "/advice/exception_page";
	}
	
	
	/*
	 * Forbidden 403
	 */
	@ExceptionHandler(AccessDeniedException.class)
	//@ResponseStatus(HttpStatus.FORBIDDEN)  <-- redirect시 에러남
	public String accessException(AccessDeniedException ex, Authentication authentication) {
		log.error("AccessDeniedException ................" + ex);
		log.error("authentication : " +  authentication);
		
		if(authentication == null) {
			log.info("go");
			return "redirect:/security/login";
		}else {
			return "redirect:/security/accessDenied";
		}
	}
	
	
	/*
	 * 404 Advice
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String hadler404() {
		log.info("404 HttpStatus.NOT_FOUND");
		
		return "/advice/404_page";
	}
	
}
