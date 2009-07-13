package com.n4systems.webservice.dto;


public class InspectionBookServiceDTO extends AbstractBaseServiceDTO {

	private String name;
	private boolean bookOpen = true;
	private long customerId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isBookOpen() {
		return bookOpen;
	}
	public void setBookOpen(boolean open) {
		this.bookOpen = open;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
