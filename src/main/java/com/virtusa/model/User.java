package com.virtusa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.virtusa.dto.UserDto;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "users")
public class User {
	/* User model to save User/Lawyer/admin
	 * Separated by role
	 * inherits lawyer to have additional lawyer information
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "user_seq")
	@SequenceGenerator(name = "user_seq", allocationSize = 1, initialValue = 1000)
	private int id;
	
	@Column(unique = true, updatable = false, nullable = false, length = 30)
	private String email;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false, length = 30)
	private String username;
	
	@Column(name = "mobile_number", nullable = false, unique = true, length = 10)
	private String mobileNumber;
	
	private boolean active;
	
	@Column(nullable = false, length = 15)
	private String role;

	private boolean enabled = true;
	
	public User() {
		super();
	}
	public User(String email, String password, String username, String mobileNumber, boolean active,
			String role) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.mobileNumber = mobileNumber;
		this.active = active;
		this.role = role;
	}
	public User(UserDto dto) {
		super();
		this.email = dto.getEmail();
		this.password = dto.getPassword();
		this.username = dto.getUsername();
		this.mobileNumber = dto.getMobileNumber();
		this.role = dto.getRole();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", username=" + username
				+ ", mobileNumber=" + mobileNumber + ", active=" + active + ", role=" + role + "]";
	}
	
}
