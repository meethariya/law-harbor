package com.virtusa.dto;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;

public class ReportDto {
	private Booking appointment;
	private int bookingId;
	@NotNull
	private String tempCaseRecord;
	private Set<CaseRecord> caseRecord;
	private Date date;
	@NotEmpty
	@Length(max = 255)
	private String reportDetail;
	private Lawyer lawyer;
	private String lawyerEmail;
	public ReportDto() {
		super();
	}
	public Booking getAppointment() {
		return appointment;
	}
	public void setAppointment(Booking appointment) {
		this.appointment = appointment;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public Set<CaseRecord> getCaseRecord() {
		return caseRecord;
	}
	public void setCaseRecord(Set<CaseRecord> caseRecord) {
		this.caseRecord = caseRecord;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getReportDetail() {
		return reportDetail;
	}
	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}
	public Lawyer getLawyer() {
		return lawyer;
	}
	public void setLawyer(Lawyer lawyer) {
		this.lawyer = lawyer;
	}
	public String getLawyerEmail() {
		return lawyerEmail;
	}
	public void setLawyerEmail(String lawyerEmail) {
		this.lawyerEmail = lawyerEmail;
	}
	public String getTempCaseRecord() {
		return tempCaseRecord;
	}
	public void setTempCaseRecord(String tempCaseRecord) {
		this.tempCaseRecord = tempCaseRecord;
	}
	@Override
	public String toString() {
		return "ReportDto [appointment=" + appointment + ", bookingId=" + bookingId + ", tempCaseRecord="
				+ tempCaseRecord + ", caseRecord=" + caseRecord + ", date=" + date + ", reportDetail=" + reportDetail
				+ ", lawyer=" + lawyer + ", lawyerEmail=" + lawyerEmail + "]";
	}
	
}
