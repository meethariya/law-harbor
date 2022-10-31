package com.virtusa.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.UserDao;
import com.virtusa.dto.BookingDto;
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.BookingAlreadyConfirmedException;
import com.virtusa.exception.IncorrectLoginDetailsException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.SlotAlreadyReservedException;
import com.virtusa.exception.UserAlreadyExistException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

@Service
public class UserService {
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	public UserService() {
		log.warn("UserService Constructor called");
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
		if(user.getRole().equals("lawyer")){			
			userDao.saveLawyer(userDtoToLawyer(user));
		}
		else {			
			userDao.saveUser(userDtoToUser(user));
		}
	}
	
	@Transactional
	public String loginUser(LoginUserDto requestedUser) {
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
		return dbUser.getRole();
	}

	@Transactional
	public User getUser(String email) {
		// returns User
		return userDao.getUser(email);
	}
	
	@Transactional
	public List<Lawyer> getAllLawyer(){
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
	
	@Transactional
	public List<CaseRecord> getUserCase(String email){
		// returns list of cases reported by user
		return userDao.getUserCase(userDao.getUser(email));
	}

	@Transactional
	public void bookAppointment(BookingDto bookingDto) {
		Booking booking = bookingDtoTobooking(bookingDto);
		if(userDao.existingBooking(booking.getLawyer(), booking.getDate())) {			
			throw new SlotAlreadyReservedException("Please change your slot");
		}
		userDao.bookAppointment(booking);		
	}
	public User userDtoToUser(UserDto myUser) {
		// converts DTO object to model object
		return new User(myUser);
	}
	
	public Lawyer userDtoToLawyer(UserDto myUser) {
		// converts DTO object to model object
		return new Lawyer(myUser);
	}
	
	@Transactional
	public Booking bookingDtoTobooking(BookingDto booking) {
		// converts DTO object to model object
		booking.setClient(getUser(booking.getUserEmail()));
		booking.setLawyer(userDao.getLawyer(booking.getLawyerEmail()));
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH");
		log.info(booking.getAppointmentDate());
		try {
			Date bookingDateTime = ft.parse(booking.getAppointmentDate()+" "+booking.getAppointmentTime());
			booking.setDateTime(bookingDateTime);
		} catch (ParseException e) {
			log.error(e.getStackTrace());
		}
		return new Booking(booking);
	}

	@Transactional
	public List<Booking> getAllBooking(String email) {
		// Returns all booking by a user
		User user = getUser(email);
		return userDao.getAllBooking(user);
		
	}
	
	@Transactional
	public void removeBooking(int id) {
		Booking booking = userDao.getBooking(id);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with id "+id);
		}
		if(booking.isBookingStatus()) {
			throw new BookingAlreadyConfirmedException("Booking is already confirmed by lawyer. Please contact Lawyer");
		}
		userDao.removeBooking(booking);
	}
}
