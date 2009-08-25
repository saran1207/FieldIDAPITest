package com.n4systems.subscription.netsuite.model;

public class Client {
	
	private String firstName;
	private String lastName;
	private String fieldId;
	private String nsrecordid;

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
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getNsrecordid() {
		return nsrecordid;
	}
	public void setNsrecordid(String nsrecordid) {
		this.nsrecordid = nsrecordid;
	}	
}
