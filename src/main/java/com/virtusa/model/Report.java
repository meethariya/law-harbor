package com.virtusa.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.virtusa.dto.ReportDto;

@Entity
@Table(name = "report")
public class Report {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "report_seq")
	@SequenceGenerator(name = "report_seq", allocationSize = 1, initialValue = 100)
	private int reportId;
	
	@OneToOne(mappedBy = "report")
	@JoinColumn(unique = true, nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Booking appointment;
	
	@OneToMany(mappedBy = "report")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<CaseRecord> caseRecord;
	
	@Column(name = "date_time")
	private Date dateTime;
	
	@Column(name = "report")
	private String reportDetail;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Lawyer issuedBy;
	
	public Report() {
		super();
	}
	public Report(Booking appointment, Set<CaseRecord> caseRecord, Date date, String reportDetail,
			Lawyer issuedBy) {
		super();
		this.appointment = appointment;
		this.caseRecord = caseRecord;
		this.dateTime = date;
		this.reportDetail = reportDetail;
		this.issuedBy = issuedBy;
	}
	public Report(ReportDto reportDto) {
		this.appointment = reportDto.getAppointment();
		this.caseRecord = reportDto.getCaseRecord();
		this.dateTime = reportDto.getDate();
		this.reportDetail = reportDto.getReportDetail();
		this.issuedBy = reportDto.getLawyer();
		
	}
	public int getReportId() {
		return reportId;
	}
	public void setReportId(int reportId) {
		this.reportId = reportId;
	}
	public Booking getAppointment() {
		return appointment;
	}
	public void setAppointment(Booking appointment) {
		this.appointment = appointment;
	}
	public Set<CaseRecord> getCaseRecord() {
		return caseRecord;
	}
	public void setCaseRecord(Set<CaseRecord> caseRecord) {
		this.caseRecord = caseRecord;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date date) {
		this.dateTime = date;
	}
	public String getReportDetail() {
		return reportDetail;
	}
	public void setReportDetail(String reportDetail) {
		this.reportDetail = reportDetail;
	}
	public Lawyer getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(Lawyer issuedBy) {
		this.issuedBy = issuedBy;
	}
	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", appointment=" + appointment.getBookingId() + 
				", date=" + dateTime + ", reportDetail=" + reportDetail + ", issuedBy=" + issuedBy + "]";
	}
	
}
