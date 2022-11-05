package com.virtusa.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

@Repository
public class AdminDao implements AdminDaoInterface{
	private static final Logger log = LogManager.getLogger(AdminDao.class);

	public AdminDao() {
		log.warn("AdminDao Constructor called");
	}

	@Autowired
	SessionFactory factory;

	@Autowired
	UserDao userDao;
	
	@Override
	public void deleteLawyer(Lawyer lawyer) {
		// Deletes Lawyer
		factory.getCurrentSession().remove(lawyer);
	}
	
	@Override
	public User getUserByNumber(String number) {
		// Gets user by Phone number. Method code written in UserDao
		return userDao.getUserByNumber(number);
	}
	
	@Override
	public Lawyer getLawyer(int lawyerId) {
		// returns lawyer by id
		return factory.getCurrentSession().get(Lawyer.class, lawyerId);
	}
}
