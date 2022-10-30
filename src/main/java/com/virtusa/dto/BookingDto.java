package com.virtusa.dto;

import java.util.Date;

import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public class BookingDto {
	private String userEmail;
	private User client;
	private String lawyerEmail;
	private Lawyer lawyer;
	private String appointmentDate;
	private String appointmentTime;
	private Date dateTime;
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
	
}
