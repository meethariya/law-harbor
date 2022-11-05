package com.virtusa.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class UserService implements UserServiceInterface{
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	public UserService() {
		log.warn("UserService Constructor called");
	}
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	@Transactional
	public void saveUser(UserDto user) {
		/* Verifies if any user exist with same email or mobile number
		 * throws UserAlreadyExistException in that case
		 * registers user otherwise
		 */
		
		// email check
		if(userDao.getUser(user.getEmail())!=null) {
			throw new UserAlreadyExistException("User Already Exist with email "+user.getEmail());
		}
		
		// Mobile Number check
		if(userDao.getUserByNumber(user.getMobileNumber())!=null) {			
			throw new UserAlreadyExistException("User Already Exist with mobile number "+user.getMobileNumber());
		}
		
		// saving user based on role
		String role = messageSource.getMessage("role.lawyer", null, "lawyer", Locale.ENGLISH);
		if(user.getRole().equals(role)){
			userDao.saveLawyer(userDtoToLawyer(user));
		}
		else {
			userDao.saveUser(userDtoToUser(user));
		}
	}
	
	@Override
	@Transactional
	public String loginUser(LoginUserDto requestedUser) {
		/* Verifies valid email and password. 
		 * Throws error in case of incorrect credentials.
		 * sets user's status online.
		 */
		
		User dbUser = getUser(requestedUser.getEmail());
		
		// validating email and password
		if (!dbUser.getPassword().equals(requestedUser.getPassword())) {
			throw new IncorrectLoginDetailsException("Incorrect Password");
		}
		
		// changing active status
		dbUser.setActive(true);
		userDao.userActiveStatusUpdate(dbUser);
		return dbUser.getRole();
	}

	@Override
	@Transactional
	public User getUser(String email) {
		// returns User
		User dbUser = userDao.getUser(email);
		
		if (dbUser==null) {
			throw new UserNotFoundException("No user with email "+email);
		}
		
		return dbUser;
	}
	
	@Override
	@Transactional
	public List<Lawyer> getAllLawyer(){
		// returns list of lawyers
		return userDao.getAllLawyer();
	}
	
	@Override
	@Transactional
	public void logoutUser(String email) {
		// changes active status of user and logout
		User user = getUser(email);
		user.setActive(false);
		userDao.userActiveStatusUpdate(user);
	}
	
	@Override
	@Transactional
	public List<CaseRecord> getUserCase(String email){
		// returns list of cases reported by user
		return userDao.getUserCase(userDao.getUser(email));
	}

	@Override
	@Transactional
	public void bookAppointment(BookingDto bookingDto) {
		// Converts dto to entity and saves the booking object
		// if given slot is already booked, throws exception

		Booking booking = bookingDtoTobooking(bookingDto);
		
		if(userDao.existingBooking(booking.getLawyer(), booking.getDate())) {			
			throw new SlotAlreadyReservedException("Please change your slot");
		}
		
		userDao.bookAppointment(booking);		
	}
	
	public User userDtoToUser(UserDto myUser) {
		// converts UserDTO object to User entity object
		return new User(myUser);
	}
	
	public Lawyer userDtoToLawyer(UserDto myUser) {
		// converts UserDTO object to Lawyer entity object
		return new Lawyer(myUser);
	}
	
	@Override
	@Transactional
	public Booking bookingDtoTobooking(BookingDto booking) {
		// converts booking DTO object to booking entity object
		
		booking.setClient(getUser(booking.getUserEmail()));
		booking.setLawyer(userDao.getLawyer(booking.getLawyerEmail()));
		
		// string date and slot from front-end to date object
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH");
		try {
			Date bookingDateTime = ft.parse(booking.getAppointmentDate()+" "+booking.getAppointmentTime());
			booking.setDateTime(bookingDateTime);
		} catch (ParseException e) {
			log.error(e.getStackTrace());
		}
		
		return new Booking(booking);
	}

	@Override
	@Transactional
	public List<Booking> getAllBooking(String email) {
		// Returns all booking by a user
		User user = getUser(email);
		return userDao.getAllBooking(user);
		
	}
	
	@Override
	@Transactional
	public void removeBooking(int id) {
		// removes booking given id. If no id found throws error.
		// if booking already confirm then throws error
		
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
