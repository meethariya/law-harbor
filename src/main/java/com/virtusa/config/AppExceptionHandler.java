package com.virtusa.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class AppExceptionHandler extends SimpleMappingExceptionResolver {
	private static final Logger log = LogManager.getLogger(AppExceptionHandler.class);

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		String errorMessage = buildLogMessage(ex, request);
	    log.error(errorMessage, ex);
	}
}
