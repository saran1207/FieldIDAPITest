package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;


public class CustomerServiceDTO extends AbstractBaseServiceDTO {
	
	private String customerId;
	private String name;
	private String contactName;
	private String contactEmail;
	private List<DivisionServiceDTO> divisions = new ArrayList<DivisionServiceDTO>();
	
	public CustomerServiceDTO() {}

	public String getCustomerId() {
    	return customerId;
    }

	public void setCustomerId(String customerId) {
    	this.customerId = customerId;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getContactName() {
    	return contactName;
    }

	public void setContactName(String contactName) {
    	this.contactName = contactName;
    }

	public String getContactEmail() {
    	return contactEmail;
    }

	public void setContactEmail(String contactEmail) {
    	this.contactEmail = contactEmail;
    }

	public List<DivisionServiceDTO> getDivisions() {
    	return divisions;
    }

	public void setDivisions(List<DivisionServiceDTO> divisions) {
    	this.divisions = divisions;
    }

}
