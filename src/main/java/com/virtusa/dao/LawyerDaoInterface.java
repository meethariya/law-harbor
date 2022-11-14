package com.virtusa.dao;

import java.util.List;

import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.Report;
import com.virtusa.model.User;

public interface LawyerDaoInterface {

	public Lawyer getLawyer(String email);

	public User getUser(String email);

	public List<Booking> getAllAppointment(Lawyer lawyer);

	public Booking getBooking(int bookingId);

	public void approveBooking(Booking appointment);

	public void cancelBooking(Booking booking);

	public List<CaseRecord> getAllCase(Lawyer lawyer);

	public void addCaseRecord(CaseRecord caseRecord);

	public void deleteCaseRecord(CaseRecord caseRecord);

	public CaseRecord getCaseRecord(int caseRecordId);

	public void updateCaseRecordReport(CaseRecord caseRecord);

	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer);

	public Report getReportByBooking(Booking booking);

	public int addReport(Report report);

	public Report getReport(int id);

	public void updateBookingReport(Booking appointment);

	public List<Booking> getBookingEarlier(Lawyer lawyer, int bookingYear);

	public List<Booking> getBookingByYear(Lawyer lawyer, int bookingYear);

	public List<CaseRecord> getAllCaseRecordEarlier(Lawyer lawyer, int caseRecordYear);

	public List<CaseRecord> getAllCaseRecordByYear(Lawyer lawyer, int caseRecordYear);

	public List<String> getUniqueUsernameForBooking(Lawyer lawyer);

	public List<Booking> getBookingByListOfUsername(Lawyer lawyer, List<String> usernames);

	public List<String> getUniqueUsernameForCaseRecord(Lawyer lawyer);

	public List<CaseRecord> getCaseRecordByListOfUsername(Lawyer lawyer, List<String> usernames);

}
