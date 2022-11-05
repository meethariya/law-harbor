package com.virtusa.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppConfig extends AbstractAnnotationConfigDispatcherServletInitializer{

	private static final Logger log = LogManager.getLogger(WebAppConfig.class);
	public WebAppConfig() {
		log.warn("WebAppConfig initialised");
	}
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { HibernateConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { ServletConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
