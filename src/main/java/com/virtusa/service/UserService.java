package com.virtusa.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.UserDao;
import com.virtusa.model.User;

@Service
public class UserService {
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	public UserService() {
		log.warn("Service Constructor called");
	}
	@Autowired
	UserDao userDao;
	
	public void saveUser(User user) {
		userDao.saveUser(user);
	}
}
