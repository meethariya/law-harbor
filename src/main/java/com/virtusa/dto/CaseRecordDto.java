package com.virtusa.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public class CaseRecordDto {
	private User user;
	@Email
	@NotEmpty
	private String userEmail;
	private Date date;
	@NotEmpty
	@Length(max = 255)
	private String eventDetail;
	@NotEmpty
	@Length(max = 255)
	private String actionTaken;
	private Lawyer issuedBy;
	public CaseRecordDto() {
		super();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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
		return "CaseRecordDto [user=" + user + ", userEmail=" + userEmail + ", date=" + date + ", eventDetail="
				+ eventDetail + ", actionTaken=" + actionTaken + ", issuedBy=" + issuedBy + "]";
	}
	
}
