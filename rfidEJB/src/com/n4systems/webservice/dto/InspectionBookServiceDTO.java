package com.n4systems.webservice.dto;


public class InspectionBookServiceDTO extends AbstractBaseDTOWithOwner {

	private String name;
	private boolean bookOpen = true;
	private boolean attachedToPrimaryOrg;

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
	public boolean isAttachedToPrimaryOrg() {
		return attachedToPrimaryOrg;
	}
	public void setAttachedToPrimaryOrg(boolean attachedToPrimaryOrg) {
		this.attachedToPrimaryOrg = attachedToPrimaryOrg;
	}

}
