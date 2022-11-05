package com.virtusa.dao;

import java.util.Date;
import java.util.List;

import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public interface UserDaoInterface {

	public void saveUser(User user);

	public void saveLawyer(Lawyer user);

	public User getUser(String email);

	public Lawyer getLawyer(String email);

	public void userActiveStatusUpdate(User user);

	public List<Lawyer> getAllLawyer();

	public User getUserByNumber(String mobileNumber);

	public List<CaseRecord> getUserCase(User user);

	public boolean existingBooking(Lawyer lawyer, Date bookingDate);

	public void bookAppointment(Booking booking);

	public List<Booking> getAllBooking(User user);

	public Booking getBooking(int id);

	public void removeBooking(Booking booking);

}
