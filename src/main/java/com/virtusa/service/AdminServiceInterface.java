package com.virtusa.service;

import java.util.List;

import com.virtusa.dto.UserDto;
import com.virtusa.model.Lawyer;
import com.virtusa.model.User;

public interface AdminServiceInterface {

	public User getUser(String email);

	public List<Lawyer> getAllLawyer();

	public void logoutUser(String email);

	public void deleteLawyer(int lawyerId);

	public void updateLawyer(UserDto lawyerDto);

	public void saveUser(UserDto lawyer);
	
}
