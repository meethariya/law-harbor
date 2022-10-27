/**
 * @author Meet Hariya
 * @company Virtusa
 * @version 1.0
 *
 */

package com.virtusa.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {
	
	public static void main(String[] args) {
    
		Configuration configuration = new Configuration();
		configuration.configure();
		SessionFactory factory = configuration.buildSessionFactory();
		Session session = factory.openSession();
		
		
		session.close();
		System.out.println("all done");
	}
}
