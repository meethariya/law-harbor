package com.virtusa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Component
public class UserDto {
	
	@NotEmpty
	@Email
	@Length(max = 30)
	private String email;
	
	@NotEmpty
	@Length(min = 8, max = 30)
	private String password;
	
	@NotEmpty
	@Length(max = 30)
	private String username;
	
	@NotEmpty
	@Length(min = 10, max = 10)
	private String mobileNumber;
		
	@NotEmpty
	private String role;
	
	private int experience;
	
	private String expertise;
	
	private String lawFirmName;

	public UserDto() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public String getLawFirmName() {
		return lawFirmName;
	}

	public void setLawFirmName(String lawFirmName) {
		this.lawFirmName = lawFirmName;
	}
	
}
