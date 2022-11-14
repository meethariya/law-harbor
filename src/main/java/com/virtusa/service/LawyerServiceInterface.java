package com.virtusa.service;

import java.util.List;
import java.util.Set;

import com.virtusa.dto.CaseRecordDto;
import com.virtusa.dto.ReportDto;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.Report;
import com.virtusa.model.User;

public interface LawyerServiceInterface {

	public Lawyer getLawyer(String email);

	public void logoutUser(String email);

	public List<Booking> getAllAppointment(String email);

	public void approveBooking(int bookingId);

	public void cancelBooking(int bookingId);

	public List<CaseRecord> getAllCase(String email);

	public User getUser(String email, String role);

	public void addCaseRecord(CaseRecordDto caseRecordDto, String role);

	public void deleteCaseRecord(int caseRecordId);

	public void editCaseRecord(CaseRecordDto caseRecordDto, int caseRecordId);

	public Booking getBooking(int bookingId);

	public List<CaseRecord> getCaseOfUser(User user, Lawyer lawyer);

	public CaseRecord getCaseRecord(int id);

	public Set<CaseRecord> stringIdToCaseRecord(String allId);

	public void addReport(ReportDto reportDto);

	public Report getReport(int id);

	public List<Booking> getBookingByYear(String email, String year);

	public List<CaseRecord> getCaseRecordByYear(String email, String year);

	public List<Booking> getBookingByUsername(String email, String username, int z);

	public List<CaseRecord> getCaseRecordByUsername(String email, String username, int z);

}
