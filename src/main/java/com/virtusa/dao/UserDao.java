package com.virtusa.dao;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.virtusa.model.User;

@Repository
public class UserDao {
	private static final Logger log = LogManager.getLogger(UserDao.class);
	public UserDao() {
		log.warn("Dao Constructor called");
	}
	@Autowired
	SessionFactory factory;
	
	@Transactional
	public void saveUser(User user) {
		factory.getCurrentSession().persist(user);
	}
}
