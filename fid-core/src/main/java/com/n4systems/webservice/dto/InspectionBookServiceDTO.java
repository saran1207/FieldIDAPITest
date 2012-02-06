package com.n4systems.webservice.dto;


public class InspectionBookServiceDTO extends AbstractBaseDTOWithOwner {

	private String name;
	private boolean bookOpen = true;
	private boolean active = true;

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
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
