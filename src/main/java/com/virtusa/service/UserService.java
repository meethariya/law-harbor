package com.virtusa.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.UserDao;
import com.virtusa.dto.BookingDto;
import com.virtusa.dto.UserDto;
import com.virtusa.exception.BookingAlreadyConfirmedException;
import com.virtusa.exception.CaseRecordNotFoundException;
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
	// user service class for logic related to user and general login/registration requests
	
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	public UserService() {
		log.warn("UserService Constructor called");
	}
	
	@Autowired
	UserDao userDao;
	
	@Override
	@Transactional
	public void saveUser(UserDto user, String role) {
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
		if(user.getRole().equals(role)){
			userDao.saveLawyer(userDtoToLawyer(user));
		}
		else {
			userDao.saveUser(userDtoToUser(user));
		}
	}
	
	@Override
	@Transactional
	public String loginUser(String email) {
		/* Verifies valid email and password. 
		 * Throws error in case of incorrect credentials.
		 * sets user's status online.
		 */
		
		User dbUser = getUser(email);
		
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

	@Override
	@Transactional
	public List<Lawyer> getLawyerByExpertise(String searchField, int z) {
		// returns list of lawyers with similar expertise
		List<String> allExpertise = userDao.getAllExpertise();		
		List<String> matchingExpertise = expertiseMatcher(allExpertise, searchField, z);
		return userDao.getLawyerByExpertise(matchingExpertise);
	}

	@Override
	@Transactional
	public List<Booking> getBookingByYear(String email, String year) {
		// checks year and returns all booking for it
		// if year is `earlier` or other string returns all bookings before 3 years

		User user = getUser(email);
		int bookingYear;
		List<Booking> output;
		try {			
			bookingYear = Integer.parseInt(year);
			output = userDao.getAllBookingByYear(user,bookingYear);
		} catch(NumberFormatException e) {			
			bookingYear = LocalDate.now().getYear()-3;
			output = userDao.getAllBookingEarlier(user, bookingYear);
		}
		if(output.isEmpty()) {
			throw new NoBookingFoundException("No booking found in "+year);
		}
		return output;
	}
	
	@Override
	@Transactional
	public List<CaseRecord> getCaseRecordByYear(String email, String year) {
		// checks year and returns list of caseRecords for it
		// if year is `earlier` or other string returns all caseRecords before three year
		
		User user = getUser(email);
		int caseRecordYear;
		List<CaseRecord> output;
		try {			
			caseRecordYear = Integer.parseInt(year);
			output = userDao.getAllCaseRecordByYear(user,caseRecordYear);
		} catch(NumberFormatException e) {			
			caseRecordYear = LocalDate.now().getYear()-3;
			output = userDao.getAllCaseRecordEarlier(user, caseRecordYear);
		}
		if(output.isEmpty()) {
			throw new CaseRecordNotFoundException("No case records in "+year);
		}
		return output;
	}

	@Override
	@Transactional
	public List<Lawyer> getLawyerByName(String searchField, int z) {
		// returns list of lawyers with similar expertise
		List<String> allName = userDao.getAllLawyerName();		
		List<String> matchingName = expertiseMatcher(allName, searchField, z);
		return userDao.getLawyerByName(matchingName);
	}
	
	@Override
	@Transactional
	public List<Booking> getBookingByLawyerName(String searchField, String email, int z) {
		// returns list of booking for given user and similar lawyer names
		User user = getUser(email);
		List<String> allName = userDao.getAllLawyerName();	
		List<String> matchingName = expertiseMatcher(allName, searchField, z);
		return userDao.getBookingByLawyerName(user, matchingName);
	}
	
	@Override
	@Transactional
	public List<CaseRecord> getCaseRecordByLawyerName(String searchField, String email, int z) {
		// returns list of case record for given user and similar lawyer names
		User user = getUser(email);
		List<String> allName = userDao.getAllLawyerName();	
		List<String> matchingName = expertiseMatcher(allName, searchField, z);
		return userDao.getCaseRecordByLawyerName(user, matchingName);
	}
	
	public List<String> expertiseMatcher(List<String> allString, String target, int z){
		// takes input as list of Strings. Matches all with target string
		// all matching using edit distance will be add to another list and will be returned
		
		List<String> output = new ArrayList<>();	// output list

		if(allString.isEmpty()) return output;	
		
		// matches all expertise retrieved from database
		for(String dbExpertise : allString) {
			if(stringMatcher(dbExpertise, target, z)) output.add(dbExpertise);
		}
		
		return output;
	}
	
	private boolean stringMatcher(String dbStrings, String target, int z) {
		// Uses DP edit distance string matching
		// returns true if text is acceptable else false
		
		if(dbStrings==null || target==null) return false;
		
		int n = target.length();					// target String length
		int m = dbStrings.length();				// text String length

		// Setting initial array
		int[][] dp = new int[n+1][m+1];
		for(int i=0; i<=n; i++) dp[i][0] = i;
		for(int j=0; j<=m; j++) dp[0][j] = j;
		
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=m; j++) {
				if(target.charAt(i-1) == dbStrings.charAt(j-1)) {
					dp[i][j]=dp[i-1][j-1];				// if char matches copy upper left int
				}
				else {
					// else find min of upper, left and upper left int and add 1 to it 
					Integer[] num = { dp[i-1][j], dp[i][j-1], dp[i-1][j-1] };
					dp[i][j] = Collections.min(Arrays.asList(num))+1;
				}
			}
		}
		// if edit distance is <= static distance it matches
		return dp[n][m] <= z;
	}
}
