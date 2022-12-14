package com.virtusa.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.virtusa.dao.LawyerDao;
import com.virtusa.dto.CaseRecordDto;
import com.virtusa.dto.ReportDto;
import com.virtusa.exception.CaseRecordNotFoundException;
import com.virtusa.exception.NoBookingFoundException;
import com.virtusa.exception.ReportAlreadyExistException;
import com.virtusa.exception.ReportNotFoundException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.Report;
import com.virtusa.model.User;


@Service
public class LawyerService implements LawyerServiceInterface{
	// lawyer service class for logic related to lawyer requests
	
	private static final Logger log = LogManager.getLogger(LawyerService.class);
	
	public LawyerService() {
		log.warn("LawyerService Constructor Called");
	}
	
	@Autowired
	LawyerDao dao;
		
	@Autowired
	UserService userService;

	@Override
	@Transactional
	public Lawyer getLawyer(String email) {
		// returns lawyer
		return dao.getLawyer(email);
	}
	
	@Override
	@Transactional
	public void logoutUser(String email) {
		// logout use. Logic in UserService
		userService.logoutUser(email);
	}
	
	@Override
	@Transactional
	public List<Booking> getAllAppointment(String email) {
		// return list of appointments
		Lawyer lawyer = getLawyer(email);
		return dao.getAllAppointment(lawyer);
	}

	@Override
	@Transactional
	public void approveBooking(int bookingId) {
		// approve an appointment given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with id "+bookingId);
		}
		dao.approveBooking(booking);
	}

	@Override
	@Transactional
	public void cancelBooking(int bookingId) {
		// reject an appointment given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with id "+bookingId);
		}
		dao.cancelBooking(booking);
	}
	
	@Override
	@Transactional
	public List<CaseRecord> getAllCase(String email){
		// returns list of case records made by lawyer
		return dao.getAllCase(getLawyer(email));
	}
	
	@Override
	@Transactional
	public User getUser(String email, String role) {
		// retrieves a user given email. Verifies the role
		// any role other than `user` will throw Exception
		User user = dao.getUser(email);
		if(user==null || !user.getRole().equals(role)) {
			throw new UserNotFoundException("No user with email "+email);
		}
		return user;
	}
	
	@Override
	@Transactional
	public void addCaseRecord(CaseRecordDto caseRecordDto, String role) {
		// Sets CaseRecordDto values required for Entity Object.
		// Converts to Case Record Entity and saves it
		caseRecordDto.setUser(getUser(caseRecordDto.getUserEmail(), role)); // throws UserNotFoundException
		caseRecordDto.setDate(new Date());
		dao.addCaseRecord(new CaseRecord(caseRecordDto));
	}
	
	@Override
	@Transactional
	public void deleteCaseRecord(int caseRecordId) {
		// Deletes caseRecord given its id
		CaseRecord caseRecord = dao.getCaseRecord(caseRecordId);
		if(caseRecord==null) {
			throw new CaseRecordNotFoundException("No Case with id "+caseRecordId);
		}
		dao.deleteCaseRecord(caseRecord);
	}
	
	@Override
	@Transactional
	public void editCaseRecord(CaseRecordDto caseRecordDto, int caseRecordId) {
		// Edits case record given details and its id
		CaseRecord dbCaseRecord = dao.getCaseRecord(caseRecordId);
		if(dbCaseRecord==null) {
			throw new CaseRecordNotFoundException("No Case with id "+caseRecordId);
		}
		// sets new date when updated
		// updates using hibernate dirty reading
		dbCaseRecord.setDate(new Date());
		dbCaseRecord.setActionTaken(caseRecordDto.getActionTaken());
		dbCaseRecord.setEventDetail(caseRecordDto.getEventDetail());
	}

	@Override
	@Transactional
	public Booking getBooking(int bookingId) {
		// returns booking given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with ID "+bookingId);
		}
		return booking;
	}

	@Override
	@Transactional
	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer) {
		// returns list of case records made by lawyer for a user.
		List<CaseRecord> caseRecords =  dao.getCaseOfUser(user, lawyer);
		if(caseRecords.isEmpty()) {
			throw new CaseRecordNotFoundException("Create a Case Record for the user first");
		}
		return caseRecords;
	}
	
	@Override
	@Transactional
	public CaseRecord getCaseRecord(int id) {
		// returns case record given its id
		CaseRecord caseRecord =  dao.getCaseRecord(id);
		if(caseRecord==null) {
			throw new CaseRecordNotFoundException("No case with ID: "+id);
		}
		return caseRecord;
	}

	@Override
	@Transactional
	public Set<CaseRecord> stringIdToCaseRecord(String allId){
		// takes input as string of IDs comma separated and returns set of case records
		if(allId==null) {
			throw new CaseRecordNotFoundException("No case Records");
		}
		
		Set<CaseRecord> allCaseRecord = new HashSet<>();
		for(String i: allId.split(",")) {
			allCaseRecord.add(getCaseRecord(Integer.parseInt(i)));
		}
		
		return allCaseRecord;
	}
	
	@Override
	@Transactional
	public void addReport(ReportDto reportDto) {
		// adds report to database and sets report value for its caserecords and booking

		// cleaning data for reportDto to report entity conversion
		reportDto.setCaseRecord(stringIdToCaseRecord(reportDto.getTempCaseRecord()));
		reportDto.setAppointment(getBooking(reportDto.getBookingId()));
		reportDto.setDate(new Date());
		
		// converting reportDto to report entity
		Report report = new Report(reportDto);

		// if booking is not found throws error
		if(dao.getReportByBooking(report.getAppointment())!=null) {
			throw new ReportAlreadyExistException("Report for this appointment already exist");
		}

		// adding report
		int id = dao.addReport(report);
		
		// fetching report just added
		Report myReport = getReport(id);

		// editing all its caserecord to set report
		for(CaseRecord i: myReport.getCaseRecord()) {
			i.setReport(myReport);
			dao.updateCaseRecordReport(i);
		}
		
		// editing booking to set report
		Booking booking = myReport.getAppointment();
		booking.setReport(myReport);
		dao.updateBookingReport(booking);
	}
	
	@Override
	@Transactional
	public Report getReport(int id) {
		// returns report given its id
		Report report =  dao.getReport(id);
		if(report==null) {
			throw new ReportNotFoundException("No report with ID "+id);
		}
		
		return report;
	}

	@Override
	@Transactional
	public List<Booking> getBookingByYear(String email, String year) {
		// checks year and returns all booking for it
		// if year is `earlier` or other string returns all bookings before 3 years

		Lawyer lawyer = getLawyer(email);
		int bookingYear;
		List<Booking> output;
		try {			
			bookingYear = Integer.parseInt(year);
			output = dao.getBookingByYear(lawyer,bookingYear);
		} catch(NumberFormatException e) {			
			bookingYear = LocalDate.now().getYear()-3;
			output = dao.getBookingEarlier(lawyer, bookingYear);
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
		
		Lawyer lawyer = getLawyer(email);
		int caseRecordYear;
		List<CaseRecord> output;
		try {			
			caseRecordYear = Integer.parseInt(year);
			output = dao.getAllCaseRecordByYear(lawyer,caseRecordYear);
		} catch(NumberFormatException e) {			
			caseRecordYear = LocalDate.now().getYear()-3;
			output = dao.getAllCaseRecordEarlier(lawyer, caseRecordYear);
		}
		if(output.isEmpty()) {
			throw new CaseRecordNotFoundException("No case records in "+year);
		}
		return output;
	}

	@Override
	@Transactional
	public List<Booking> getBookingByUsername(String email, String username, int z) {
		// returns list of booking for given lawyer email and matchinf username for user
		Lawyer lawyer = getLawyer(email);
		List<String> allUsername = dao.getUniqueUsernameForBooking(lawyer);
		List<String> matchingUsername = userService.expertiseMatcher(allUsername, username, z);
		List<Booking> output = dao.getBookingByListOfUsername(lawyer, matchingUsername);
		if (output.isEmpty()) {
			throw new NoBookingFoundException("No booking found for users with name: "+username);
		}
		return output;
	}

	@Override
	@Transactional
	public List<CaseRecord> getCaseRecordByUsername(String email, String username, int z) {
		// returns list of case record for given lawyer email and matchinf username for user
		Lawyer lawyer = getLawyer(email);
		List<String> allUsername = dao.getUniqueUsernameForCaseRecord(lawyer);
		List<String> matchingUsername = userService.expertiseMatcher(allUsername, username, z);
		List<CaseRecord> output = dao.getCaseRecordByListOfUsername(lawyer, matchingUsername);
		if (output.isEmpty()) {
			throw new CaseRecordNotFoundException("No Case Record found for users with name: "+username);
		}
		return output;
	}
}
