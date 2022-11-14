package com.virtusa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class EditLawyerDto {
	// DTO class for editing Lawyer model
	// performs validations without password validation
	
	@NotEmpty
	@Email
	@Length(max = 30)
	private String email;
	
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
	
	private float charge;
	
	public EditLawyerDto() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public float getCharge() {
		return charge;
	}

	public void setCharge(float charge) {
		this.charge = charge;
	}

	@Override
	public String toString() {
		return "UserDto [email=" + email + ", username=" + username + ", mobileNumber="
				+ mobileNumber + ", role=" + role + ", experience=" + experience + ", expertise=" + expertise
				+ ", lawFirmName=" + lawFirmName + "]";
	}

	
}
