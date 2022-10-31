package com.virtusa.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.LawyerDao;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.Lawyer;


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
}
