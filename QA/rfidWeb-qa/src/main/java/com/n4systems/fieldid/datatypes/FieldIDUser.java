package com.n4systems.fieldid.datatypes;

/**
 * Used for requesting an account.
 * 
 */
public class FieldIDUser {

	String userID = null;
	String email = null;
	String firstName = null;
	String lastName = null;
	String position = null;
	String timeZone = null;
	String companyName = null;
	String phoneNumber = null;
	String password = null;
	String comments = null;
	
	public FieldIDUser(String userID, String email, String firstName, String lastName, String position, String timeZone, String companyName, String phoneNumber, String password, String comments) {
		this.userID = userID;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.timeZone = timeZone;
		this.companyName = companyName;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.comments = comments;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public void setEmailAddress(String email) {
		this.email = email;
	}
	
	public String getEmailAddress() {
		return email;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
		
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}
}
