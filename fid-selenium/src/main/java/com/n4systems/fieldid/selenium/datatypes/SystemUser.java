package com.n4systems.fieldid.selenium.datatypes;

public class SystemUser {
	private String userid;
	private String email;
	private String securityRFIDNumber;
	private String password;
	private String verifyPassword;
	private Owner owner;
	private String firstName;
	private String lastName;
	private String initials;
	private String position;
	private String country;
	private String timeZone;

	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("\n             User ID: " + (userid != null ? userid : "{default}"));
		s.append("\n       Email Address: " + (email != null ? email : "{default}"));
		s.append("\nSecurity RFID Number: " + (securityRFIDNumber != null ? securityRFIDNumber : "{default}"));
		s.append("\n            Password: " + (password != null ? password : "{default}"));
		s.append("\n     Verify Password: " + (verifyPassword != null ? verifyPassword : "{default}"));
		s.append(owner);
		s.append("\n          First Name: " + (firstName != null ? firstName : "{default}"));
		s.append("\n           Last Name: " + (lastName != null ? lastName : "{default}"));
		s.append("\n            Initials: " + (initials != null ? initials : "{default}"));
		s.append("\n            Position: " + (position != null ? position : "{default}"));
		s.append("\n             Country: " + (country != null ? country : "{default}"));
		s.append("\n           Time Zone: " + (timeZone != null ? timeZone : "{default}"));
		return s.toString();
	}
	
	public SystemUser(String userid, String email, String password, String verifyPassword, Owner owner, String firstName, String lastName) {
		super();
		this.userid = userid;
		this.email = email;
		this.password = password;
		this.verifyPassword = verifyPassword;
		this.owner = owner;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSecurityRFIDNumber() {
		return securityRFIDNumber;
	}
	
	public void setSecurityRFIDNumber(String securityRFIDNumber) {
		this.securityRFIDNumber = securityRFIDNumber;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getVerifyPassword() {
		return verifyPassword;
	}
	
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public Owner getOwner() {
		return owner;
	}
	
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getInitials() {
		return initials;
	}
	
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
