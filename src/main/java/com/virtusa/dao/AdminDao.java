package com.virtusa.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

@Repository
public class AdminDao {
	private static final Logger log = LogManager.getLogger(AdminDao.class);

	public AdminDao() {
		log.warn("AdminDao Constructor called");
	}

	@Autowired
	SessionFactory factory;

	@Autowired
	UserDao userDao;
	
	public void deleteLawyer(Lawyer lawyer) {
		factory.getCurrentSession().remove(lawyer);
	}
	
	public User getUserByNumber(String number) {
		return userDao.getUserByNumber(number);
	}
	public Lawyer getLawyer(int lawyerId) {
		return factory.getCurrentSession().get(Lawyer.class, lawyerId);
	}
}
