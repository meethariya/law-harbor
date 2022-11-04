package com.virtusa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.virtusa.dto.CaseRecordDto;

@Entity
@Table(name = "case")
public class CaseRecord {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int caseRecordId;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@Column(name="date_time", nullable = false)
	private Date date;
	
	@Column(name="event_detail", nullable = false)
	private String eventDetail;
	
	@Column(name="action_taken", nullable = false)
	private String actionTaken;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Lawyer issuedBy;
	
	@ManyToOne()
	private Report report;
	
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	public CaseRecord() {
		super();
	}
	public CaseRecord(User user, Date date, String eventDetail, String actionTaken, Lawyer issuedBy) {
		super();
		this.user = user;
		this.date = date;
		this.eventDetail = eventDetail;
		this.actionTaken = actionTaken;
		this.issuedBy = issuedBy;
	}
	public CaseRecord(CaseRecordDto caseRecordDto) {
		super();
		this.user = caseRecordDto.getUser();
		this.date = caseRecordDto.getDate();
		this.eventDetail = caseRecordDto.getEventDetail();
		this.actionTaken = caseRecordDto.getActionTaken();
		this.issuedBy = caseRecordDto.getIssuedBy();
	}
	public int getCaseRecordId() {
		return caseRecordId;
	}
	public void setCaseRecordId(int caseRecordId) {
		this.caseRecordId = caseRecordId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getEventDetail() {
		return eventDetail;
	}
	public void setEventDetail(String eventDetail) {
		this.eventDetail = eventDetail;
	}
	public String getActionTaken() {
		return actionTaken;
	}
	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}
	public Lawyer getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(Lawyer issuedBy) {
		this.issuedBy = issuedBy;
	}
	@Override
	public String toString() {
		return "CaseRecord [caseRecordId=" + caseRecordId + ", user=" + user + ", date=" + date + ", eventDetail="
				+ eventDetail + ", actionTaken=" + actionTaken + ", issuedBy=" + issuedBy + ", report=" + report + "]";
	}
	
}
