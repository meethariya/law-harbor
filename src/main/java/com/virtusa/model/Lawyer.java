package com.virtusa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.virtusa.dto.UserDto;

@Entity
@Table(name = "lawyer")
@PrimaryKeyJoinColumn(name="id")
public class Lawyer extends User{
	/* Lawyer model inherited by User model
	 * Strategy is joined, so separated tables joined by user id
	 */
	
	private int experience;
	
	@Column(length = 30)
	private String expertise;
	
	@Column(name="law_firm_name", length = 30)
	private String lawFirmName;

	public Lawyer() {
		super();
	}

	public Lawyer(int experience, String expertise, String lawFirmName) {
		super();
		this.experience = experience;
		this.expertise = expertise;
		this.lawFirmName = lawFirmName;
	}

	public Lawyer(UserDto user) {
		super(user);
		this.experience = user.getExperience();
		this.expertise = user.getExpertise();
		this.lawFirmName = user.getLawFirmName();
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

	@Override
	public String toString() {
		return "Lawyer [experience=" + experience + ", expertise=" + expertise + ", lawFirmName=" + lawFirmName
				+ ", getId()=" + getId() + ", getEmail()=" + getEmail() + ", getPassword()=" + getPassword()
				+ ", getUsername()=" + getUsername() + ", getMobileNumber()=" + getMobileNumber() + ", isActive()="
				+ isActive() + "]";
	}

	
	
}
