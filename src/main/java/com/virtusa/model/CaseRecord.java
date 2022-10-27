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
	private User userId;
	
	@Column(name="date_time", nullable = false)
	private Date date;
	
	@Column(name="event_detail", nullable = false)
	private String eventDetail;
	
	@Column(name="action_taken", nullable = false)
	private String actionTaken;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User issuedBy;
	
	public CaseRecord() {
		super();
	}
	public CaseRecord(User userId, Date date, String eventDetail, String actionTaken, User issuedBy) {
		super();
		this.userId = userId;
		this.date = date;
		this.eventDetail = eventDetail;
		this.actionTaken = actionTaken;
		this.issuedBy = issuedBy;
	}
	public int getCaseRecordId() {
		return caseRecordId;
	}
	public void setCaseRecordId(int caseRecordId) {
		this.caseRecordId = caseRecordId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
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
	public User getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(User issuedBy) {
		this.issuedBy = issuedBy;
	}
	@Override
	public String toString() {
		return "CaseRecord [caseRecordId=" + caseRecordId + ", userId=" + userId + ", date=" + date + ", eventDetail="
				+ eventDetail + ", actionTaken=" + actionTaken + ", issuedBy=" + issuedBy + "]";
	}
	
}
