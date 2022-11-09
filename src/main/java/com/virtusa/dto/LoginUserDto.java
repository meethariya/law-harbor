package com.virtusa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

public class LoginUserDto {
	// DTO class for login user/admin/lawyer
	// performs validations
	
	@NotEmpty
	@Email
	@Length(max = 30)
	private String email;
	
	@NotEmpty
	@Length(min = 8, max = 30)
	private String password;
	
	public LoginUserDto() {
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
	
}
