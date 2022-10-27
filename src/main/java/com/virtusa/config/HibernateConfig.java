package com.virtusa.config;

import java.util.Properties;

import javax.sql.DataSource;

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
@ComponentScan(basePackages = {"com.virtusa.dao","com.virtusa.service","com.virtusa.model"})
public class HibernateConfig {

	// Database configuration
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource mySource = new DriverManagerDataSource();
		mySource.setDriverClassName("oracle.jdbc.OracleDriver");
		mySource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		mySource.setUsername("meet");
		mySource.setPassword("hariya");
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
}
