package com.n4systems.fieldid.datatypes;

public class ScheduleSearchCriteria {

	String scheduleStatus;
	String serialNumber;
	String customer;
	String division;
	String eventTypeGroup;
	String productType;
	String productStatus;
	String fromDate;
	String toDate;
	
	public ScheduleSearchCriteria() {
	}
	
	public void setScheduleStatus(String s) {
		this.scheduleStatus = s;
	}
	
	public void setSerialNumber(String s) {
		this.serialNumber = s;
	}
	
	public void setCustomer(String s) {
		this.customer = s;
	}
	
	public void setDivision(String s) {
		this.division = s;
	}
	
	public void setEventTypeGroup(String s) {
		this.eventTypeGroup = s;
	}
	
	public void setProductType(String s) {
		this.productType = s;
	}
	
	public void setProductStatus(String s) {
		this.productStatus = s;
	}
	
	public void setFromDate(String s) {
		this.fromDate = s;
	}
	
	public void setToDate(String s) {
		this.toDate = s;
	}

	public String getScheduleStatus() {
		return scheduleStatus;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getDivision() {
		return division;
	}
	
	public String getEventTypeGroup() {
		return eventTypeGroup;
	}
	
	public String getProductType() {
		return productType;
	}
	
	public String getProductStatus() {
		return productStatus;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
}
