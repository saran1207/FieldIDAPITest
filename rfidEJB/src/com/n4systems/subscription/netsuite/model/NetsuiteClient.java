package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.Person;
import com.n4systems.subscription.ExternalIdResponse;

public class NetsuiteClient implements Person, ExternalIdResponse {
	
	private String firstName;
	private String lastName;
	private String fieldId;
	private Long nsrecordid;

	public Long getExternalId() {
		return getNsrecordid();
	}

	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
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

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
}
