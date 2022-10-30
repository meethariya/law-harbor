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
	private User client;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User lawyer;
	
	@Column(name = "date_time", nullable = false)
	private Date date;
	
	@Column(name = "status")
	private boolean bookingStatus;
	
	public Booking() {
		super();
	}
	public Booking(User client, User lawyer, Date date, boolean bookingStatus) {
		super();
		this.client = client;
		this.lawyer = lawyer;
		this.date = date;
		this.bookingStatus = bookingStatus;
	}
	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public User getLawyer() {
		return lawyer;
	}
	public void setLawyer(User lawyer) {
		this.lawyer = lawyer;
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
		return "Booking [bookingId=" + bookingId + ", client=" + client + ", lawyer=" + lawyer
				+ ", date=" + date + ", bookingStatus=" + bookingStatus + "]";
	}
	
	
}
