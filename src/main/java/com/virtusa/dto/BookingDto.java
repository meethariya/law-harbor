package com.virtusa.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public class BookingDto {
	// DTO class for booking model
	// performs validations
	
	private String userEmail;
	private User client;
	private String lawyerEmail;
	private Lawyer lawyer;
	@NotEmpty
	private String appointmentDate;
	@NotEmpty
	private String appointmentTime;
	private Date dateTime;
	@NotEmpty
	@Length(max = 30)
	private String subject;
	@NotEmpty
	@Length(max = 30)
	private String topic;
	public BookingDto() {
		super();
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String date) {
		this.appointmentDate = date;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String time) {
		this.appointmentTime = time;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public Lawyer getLawyer() {
		return lawyer;
	}
	public void setLawyer(Lawyer userLawyer) {
		this.lawyer = userLawyer;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getLawyerEmail() {
		return lawyerEmail;
	}
	public void setLawyerEmail(String lawyerEmail) {
		this.lawyerEmail = lawyerEmail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
}
