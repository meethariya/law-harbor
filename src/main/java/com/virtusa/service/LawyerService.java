package com.virtusa.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.LawyerDao;
import com.virtusa.dto.CaseRecordDto;
import com.virtusa.exception.CaseRecordNotFound;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;


@Service
public class LawyerService {
	private static final Logger log = LogManager.getLogger(LawyerService.class);
	
	public LawyerService() {
		log.warn("LawyerService Constructor Called");
	}
	
	@Autowired
	LawyerDao dao;
	
	@Autowired
	UserService userService;

	@Transactional
	public Lawyer getLawyer(String email) {
		// returns lawyer
		return dao.getLawyer(email);
	}
	
	@Transactional
	public void logoutUser(String email) {
		// logout user
		userService.logoutUser(email);
	}
	
	@Transactional
	public List<Booking> getAllAppointment(String email) {
		// return list of appointments
		Lawyer lawyer = getLawyer(email);
		return dao.getAllAppointment(lawyer);
	}

	@Transactional
	public void approveBooking(int bookingId) {
		// approve an appointment given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with id "+bookingId);
		}
		dao.approveBooking(bookingId);
	}

	@Transactional
	public void cancelBooking(int bookingId) {
		// reject an appointment given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with id "+bookingId);
		}
		dao.cancelBooking(booking);
	}
	
	@Transactional
	public List<CaseRecord> getAllCase(String email){
		// returns list of case records made by lawyer
		return dao.getAllCase(getLawyer(email));
	}
	
	@Transactional
	public User getUser(String email) {
		User user = dao.getUser(email);
		if(user==null || !user.getRole().equals("user")) {
			throw new UserNotFoundException("No user with email "+email);
		}
		return user;
	}
	
	@Transactional
	public void addCaseRecord(CaseRecordDto caseRecordDto) {			
		caseRecordDto.setUser(getUser(caseRecordDto.getUserEmail())); // throws UserNotFoundException
		caseRecordDto.setDate(new Date());
		dao.addCaseRecord(new CaseRecord(caseRecordDto));
	}
	@Transactional
	public void deleteCaseRecord(int caseRecordId) {
		CaseRecord caseRecord = dao.getCaseRecord(caseRecordId);
		if(caseRecord==null) {
			throw new CaseRecordNotFound("No Case with id "+caseRecordId);
		}
		dao.deleteCaseRecord(caseRecord);
	}
	@Transactional
	public void editCaseRecord(CaseRecordDto caseRecordDto, int caseRecordId) {
		CaseRecord dbCaseRecord = dao.getCaseRecord(caseRecordId);
		if(dbCaseRecord==null) {
			throw new CaseRecordNotFound("No Case with id "+caseRecordId);
		}
		dbCaseRecord.setDate(new Date());
		dbCaseRecord.setActionTaken(caseRecordDto.getActionTaken());
		dbCaseRecord.setEventDetail(caseRecordDto.getEventDetail());
	}
}
