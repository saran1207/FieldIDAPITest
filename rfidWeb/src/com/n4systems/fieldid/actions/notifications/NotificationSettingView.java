package com.n4systems.fieldid.actions.notifications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation
public class NotificationSettingView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long ownerId;
	private Long createdTimeStamp;
	private String name;
	private String frequency;
	private String periodStart;
	private String periodEnd;
	private Long productTypeId;
	private Long inspectionTypeId;
	private Long customerId;
	private Long divisionId;
	private Long jobSiteId;
	private List<String> addresses = new ArrayList<String>();

	public NotificationSettingView() {}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getID() {
		return id;
	}
	
	public void setID(Long id) {
		this.id = id;
	}
	
	public Long getOwnerId() {
    	return ownerId;
    }

	public void setOwnerId(Long ownerId) {
    	this.ownerId = ownerId;
    }
	
	public Long getCreatedTimeStamp() {
    	return createdTimeStamp;
    }

	public void setCreatedTimeStamp(Long createdTimeStamp) {
    	this.createdTimeStamp = createdTimeStamp;
    }

	public String getName() {
    	return name;
    }

	@RequiredStringValidator( message = "", key = "error.namerequired" )
	public void setName(String name) {
    	this.name = name;
    }

	public String getFrequency() {
    	return frequency;
    }

	public void setFrequency(String frequency) {
    	this.frequency = frequency;
    }

	public String getPeriodStart() {
    	return periodStart;
    }

	public void setPeriodStart(String periodStart) {
    	this.periodStart = periodStart;
    }

	public String getPeriodEnd() {
    	return periodEnd;
    }

	public void setPeriodEnd(String periodEnd) {
    	this.periodEnd = periodEnd;
    }

	public Long getProductTypeId() {
    	return productTypeId;
    }

	public void setProductTypeId(Long productTypeId) {
    	this.productTypeId = productTypeId;
    }

	public Long getInspectionTypeId() {
    	return inspectionTypeId;
    }

	public void setInspectionTypeId(Long inspectionTypeId) {
    	this.inspectionTypeId = inspectionTypeId;
    }

	public Long getCustomerId() {
    	return customerId;
    }

	public void setCustomerId(Long customerId) {
    	this.customerId = customerId;
    }

	public Long getDivisionId() {
    	return divisionId;
    }

	public void setDivisionId(Long divisionId) {
    	this.divisionId = divisionId;
    }

	public Long getJobSiteId() {
    	return jobSiteId;
    }

	public void setJobSiteId(Long jobSiteId) {
    	this.jobSiteId = jobSiteId;
    }

	public List<String> getAddresses() {
    	return addresses;
    }

	@EmailValidator(message="", key = "error.emailformat")
	public void setAddresses(List<String> addresses) {
    	this.addresses = addresses;
    }
}
