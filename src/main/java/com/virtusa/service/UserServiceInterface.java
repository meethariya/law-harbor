package com.virtusa.service;

import java.util.List;

import com.virtusa.dto.BookingDto;
import com.virtusa.dto.LoginUserDto;
import com.virtusa.dto.UserDto;
import com.virtusa.model.Booking;
import com.virtusa.model.CaseRecord;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public interface UserServiceInterface {

	public void saveUser(UserDto user);

	public String loginUser(LoginUserDto requestedUser);

	public User getUser(String email);

	public List<Lawyer> getAllLawyer();

	public void logoutUser(String email);

	public List<CaseRecord> getUserCase(String email);

	public void bookAppointment(BookingDto bookingDto);

	public Booking bookingDtoTobooking(BookingDto booking);

	public List<Booking> getAllBooking(String email);

	public void removeBooking(int id);

}
