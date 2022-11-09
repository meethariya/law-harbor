package com.virtusa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.AdminDao;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

@Service
public class AdminService implements AdminServiceInterface {
	// admin service class for logic related to admin requests
	private static final Logger log = LogManager.getLogger(AdminService.class);
	
	public AdminService() {
		log.warn("AdminService Constructor called");
	}
	
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	UserService userService;

	@Autowired
	LawyerService lawyerService;
	
	@Override
	@Transactional
	public User getUser(String email) {
		// returns User from UserService
		return userService.getUser(email);
	}
	
	@Override
	@Transactional
	public List<Lawyer> getAllLawyer(){
		// returns list of lawyers from UserService
		return userService.getAllLawyer();
	}
	
	@Override
	@Transactional
	public void logoutUser(String email) {
		// logout user. Logic in UserService
		userService.logoutUser(email);		
	}
	
	@Override
	@Transactional
	public void deleteLawyer(int lawyerId) {
		// deletes lawyer
		Lawyer lawyer = adminDao.getLawyer(lawyerId);
		if(lawyer==null) {
			throw new UserNotFoundException("Not Lawyer with id "+lawyerId);
		}
		adminDao.deleteLawyer(lawyer);
	}
	
	@Override
	@Transactional
	public void updateLawyer(UserDto lawyerDto) {
		// updates Lawyer Info
		
		// if another user with same number exist then throws error
		User user = adminDao.getUserByNumber(lawyerDto.getMobileNumber());
		if(user!=null && !user.getEmail().equals(lawyerDto.getEmail())) {			
			throw new UserAlreadyExistException("User Already Exist with mobile number "+lawyerDto.getMobileNumber());
		}
		
		// updating lawyer information using hibernate dirty reading
		// retrieves lawyer from LawyerService
		Lawyer dbLawyer = lawyerService.getLawyer(lawyerDto.getEmail());
		dbLawyer.setExperience(lawyerDto.getExperience());
		dbLawyer.setExpertise(lawyerDto.getExpertise());
		dbLawyer.setLawFirmName(lawyerDto.getLawFirmName());
		dbLawyer.setMobileNumber(lawyerDto.getMobileNumber());
		dbLawyer.setUsername(lawyerDto.getUsername());
	}
	
	@Override
	@Transactional
	public void saveUser(UserDto lawyer) {
		// adds new lawyer
		userService.saveUser(lawyer);
	}
}
