package com.virtusa.config;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.virtusa.controller"})
public class ServletConfig implements WebMvcConfigurer{
	// sets server settings for the app
	// generally replacement for dispatcher-servlet.xml
	
	private static final Logger log = LogManager.getLogger(ServletConfig.class);
	public ServletConfig() {
		log.warn("Servlet Config initialised");
	}
	// View Resolver
	@Bean
	public InternalResourceViewResolver resolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setViewClass(JstlView.class);
		vr.setPrefix("/WEB-INF/views/");
		vr.setSuffix(".jsp");
		return vr;
	}
	
	// Resource Bundler
	@Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("errormessages","staticmessages");
        return messageSource;
    }
	
	// static resource handler
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");	
    }
	
	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {

		SimpleMappingExceptionResolver resolver = new AppExceptionHandler();
		
		Properties exceptionMappings = new Properties();
        exceptionMappings.put(Exception.class.getName(), "Error");
        
        resolver.setExceptionMappings(exceptionMappings);
        resolver.addStatusCode("Error", 500);
        
		return resolver;
	}
	
}
