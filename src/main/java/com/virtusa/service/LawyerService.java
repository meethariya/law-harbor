package com.virtusa.service;

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
import com.virtusa.exception.ReportALreadyExistException;
import com.virtusa.exception.ReportNotFoundException;
import com.virtusa.exception.UserNotFoundException;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.Report;
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
		// logout use. Logic in UserService
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
		dao.approveBooking(booking);
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
		// retrieves a user given email. Verifies the role
		// any role other than `user` will throw Exception
		User user = dao.getUser(email);
		if(user==null || !user.getRole().equals("user")) {
			throw new UserNotFoundException("No user with email "+email);
		}
		return user;
	}
	
	@Transactional
	public void addCaseRecord(CaseRecordDto caseRecordDto) {
		// Sets CaseRecordDto values required for Entity Object.
		// Converts to Case Record Entity and saves it
		caseRecordDto.setUser(getUser(caseRecordDto.getUserEmail())); // throws UserNotFoundException
		caseRecordDto.setDate(new Date());
		dao.addCaseRecord(new CaseRecord(caseRecordDto));
	}
	@Transactional
	public void deleteCaseRecord(int caseRecordId) {
		// Deletes caseRecord given its id
		CaseRecord caseRecord = dao.getCaseRecord(caseRecordId);
		if(caseRecord==null) {
			throw new CaseRecordNotFoundException("No Case with id "+caseRecordId);
		}
		dao.deleteCaseRecord(caseRecord);
	}
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

	@Transactional
	public Booking getBooking(int bookingId) {
		// returns booking given its id
		Booking booking = dao.getBooking(bookingId);
		if(booking==null) {
			throw new NoBookingFoundException("No appointment with ID "+bookingId);
		}
		return booking;
	}

	@Transactional
	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer) {
		// returns list of case records made by lawyer for a user.
		List<CaseRecord> caseRecords =  dao.getCaseOfUser(user, lawyer);
		if(caseRecords.isEmpty()) {
			throw new CaseRecordNotFoundException("Create a Case Record for the user first");
		}
		return caseRecords;
	}
	
	@Transactional
	public CaseRecord getCaseRecord(int id) {
		// returns case record given its id
		CaseRecord caseRecord =  dao.getCaseRecord(id);
		if(caseRecord==null) {
			throw new CaseRecordNotFoundException("No case with ID: "+id);
		}
		return caseRecord;
	}

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
			throw new ReportALreadyExistException("Report for this appointment already exist");
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
	
	@Transactional
	public Report getReport(int id) {
		// returns report given its id
		Report report =  dao.getReport(id);
		if(report==null) {
			throw new ReportNotFoundException("No report with ID "+id);
		}
		
		return report;
	}
}
