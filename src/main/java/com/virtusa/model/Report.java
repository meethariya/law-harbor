package com.virtusa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "report")
public class Report {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int reportId;
	
	@OneToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Booking appointment;
	
	@OneToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private CaseRecord caseRecord;
	
	@Column(name = "date_time")
	private Date dateTime;
	
	@Column(name = "report")
	private String reportDetail;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User issuedBy;
	
	public Report() {
		super();
	}
	public Report(Booking appointment, CaseRecord caseRecord, Date date, String reportDetail,
			User issuedBy) {
		super();
		this.appointment = appointment;
		this.caseRecord = caseRecord;
		this.dateTime = date;
		this.reportDetail = reportDetail;
		this.issuedBy = issuedBy;
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
	public CaseRecord getCaseRecord() {
		return caseRecord;
	}
	public void setCaseRecord(CaseRecord caseRecord) {
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
	public User getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(User issuedBy) {
		this.issuedBy = issuedBy;
	}
	@Override
	public String toString() {
		return "Report [reportId=" + reportId + ", appointment=" + appointment + ", caseRecord="
				+ caseRecord + ", date=" + dateTime + ", reportDetail=" + reportDetail + ", issuedBy=" + issuedBy + "]";
	}
	
}
