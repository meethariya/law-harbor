package com.virtusa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.UserDao;
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.User;

@Service
public class UserService {
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	public UserService() {
		log.warn("Service Constructor called");
	}
	
	@Autowired
	UserDao userDao;
	
	@Transactional
	public void saveUser(UserDto user) {
		// Verifies if any user exist with same email or mobile number
		// throws UserAlreadyExistException in that case
		// registers user otherwise
		if(userDao.getUser(user.getEmail())!=null) {
			throw new UserAlreadyExistException("User Already Exist with email "+user.getEmail());
		}
		
		if(userDao.getUserByNumber(user.getMobileNumber())!=null) {			
			throw new UserAlreadyExistException("User Already Exist with mobile number "+user.getMobileNumber());
		}
		
		userDao.saveUser(userDtoToUser(user));
	}
	
	@Transactional
	public void loginUser(LoginUserDto requestedUser) {
		// Verifies valid email and password. Throws error in case of incorrect credentials.
		// sets user's status online.
		User dbUser = getUser(requestedUser.getEmail());
		if (dbUser==null) {
			throw new UserNotFoundException("No user with email "+requestedUser.getEmail());
		}
		
		if (!dbUser.getPassword().equals(requestedUser.getPassword())) {
			throw new IncorrectLoginDetailsException("Incorrect Password");
		}
		
		dbUser.setActive(true);
		userDao.userActiveStatusUpdate(dbUser);
	}

	@Transactional
	public User getUser(String email) {
		// returns User
		return userDao.getUser(email);
	}
	
	@Transactional
	public List<User> getAllLawyer(){
		// returns list of lawyers
		return userDao.getAllLawyer();
	}
	
	@Transactional
	public void logoutUser(String email) {
		// changes active status of user and logout
		User user = getUser(email);
		user.setActive(false);
		userDao.userActiveStatusUpdate(user);
	}
	
	public User userDtoToUser(UserDto myUser) {
		// converts DTO object to model object
		return new User(myUser);
	}
	
	@Transactional
	public List<CaseRecord> getUserCase(String email){
		// returns list of cases reported by user
		return userDao.getUserCase(userDao.getUser(email));
	}
}
