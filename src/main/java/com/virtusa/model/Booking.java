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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.virtusa.dto.BookingDto;

@Entity
@Table(name = "appointment")
public class Booking {
	/* booking model to save appointment details
	 * User		1-*		Booking
	 * Lawyer	1-*		Booking
	 * Report	1-1		Booking
	 */
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "booking_seq")
	@SequenceGenerator(name = "booking_seq", allocationSize = 1, initialValue = 1)
	private int bookingId;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User client;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Lawyer lawyer;
	
	@Column(name = "date_time", nullable = false)
	private Date date;
	
	@Column(name = "status")
	private boolean bookingStatus;
	
	@Column(nullable = false, length = 30)
	private String subject;
	
	@OneToOne
	private Report report;
	
	public Booking() {
		super();
	}
	public Booking(User client, Lawyer lawyer, Date date, boolean bookingStatus) {
		super();
		this.client = client;
		this.lawyer = lawyer;
		this.date = date;
		this.bookingStatus = bookingStatus;
	}
	public Booking(BookingDto booking) {
		super();
		this.client = booking.getClient();
		this.lawyer = booking.getLawyer();
		this.date = booking.getDateTime();
		this.subject = booking.getSubject();
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
	public Lawyer getLawyer() {
		return lawyer;
	}
	public void setLawyer(Lawyer lawyer) {
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}
	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", client=" + client + ", lawyer=" + lawyer + ", date=" + date
				+ ", bookingStatus=" + bookingStatus + ", subject=" + subject + ", report=" + report + "]";
	}
	
	
	
}
