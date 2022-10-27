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
@Table(name = "appointment")
public class Booking {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bookingId;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User clientDetail;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User lawyerDetail;
	
	@Column(name = "law_firm_name", length = 30, nullable = false)
	private String lawfirmName;
	
	@Column(name = "date_time", nullable = false)
	private Date date;
	
	@Column(name = "status")
	private boolean bookingStatus;
	
	public Booking() {
		super();
	}
	public Booking(User clientDetail, User lawyerDetail, String lawfirmName, Date date, boolean bookingStatus) {
		super();
		this.clientDetail = clientDetail;
		this.lawyerDetail = lawyerDetail;
		this.lawfirmName = lawfirmName;
		this.date = date;
		this.bookingStatus = bookingStatus;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public User getClientDetail() {
		return clientDetail;
	}
	public void setClientDetail(User clientDetail) {
		this.clientDetail = clientDetail;
	}
	public User getLawyerDetail() {
		return lawyerDetail;
	}
	public void setLawyerDetail(User lawyerDetail) {
		this.lawyerDetail = lawyerDetail;
	}
	public String getLawfirmName() {
		return lawfirmName;
	}
	public void setLawfirmName(String lawfirmName) {
		this.lawfirmName = lawfirmName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(boolean bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", clientDetail=" + clientDetail + ", lawyerDetail=" + lawyerDetail
				+ ", lawfirmName=" + lawfirmName + ", date=" + date + ", bookingStatus=" + bookingStatus + "]";
	}
	
}
