package com.virtusa.dto;

public class BookingData {
	private String userEmail;
	private int lawyerId;
	private String date;
	private String time;
	public BookingData() {
		super();
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public int getLawyerId() {
		return lawyerId;
	}
	public void setLawyerId(int lawyerId) {
		this.lawyerId = lawyerId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
