package com.virtusa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.virtusa.dao","com.virtusa.service","com.virtusa.model","com.virtusa.dto"})
public class HibernateConfig {

	private static final Logger log = LogManager.getLogger(HibernateConfig.class);
	public HibernateConfig(){
		log.warn("Hibernate Config initialised");
	}
	
	@Autowired
	MessageSource messageSource;
	
	// Database configuration
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource mySource = new DriverManagerDataSource();
		
		// loading dbConfig file in properties
		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dbConfig.properties");
		try {
			prop.load(inputStream);
		}catch(IOException e) {
			log.error(e);
		}
		
		mySource.setDriverClassName(prop.getProperty("drivername"));
		mySource.setUrl(prop.getProperty("url"));
		mySource.setUsername(prop.getProperty("username"));
		mySource.setPassword(prop.getProperty("password"));
		
		return mySource;
	}
	
	// Hibernate Session factory configuration
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
		sf.setDataSource(dataSource());
		sf.setPackagesToScan("com.virtusa.model");
		
		Properties prop = new Properties();
		prop.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		prop.put("hibernate.show_sql","true");
		prop.put("hibernate.hbm2ddl.auto", "update");
		
		sf.setHibernateProperties(prop);
		return sf;
	}
	
	// Hibernate Transaction Manager
	@Bean
	public PlatformTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}
	
	public String getValueFromProperties(String key, String defaultValue) {
		// returns properties based on key and default value
		return messageSource.getMessage(key, null, defaultValue, Locale.ENGLISH);
	}
}
