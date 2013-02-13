package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.List;

public class ApiMultiAddEvent {
	private List<ApiMultiAddEventItem> items;
	private ApiEvent eventTemplate;
	private boolean copyAssignedTo;
	private boolean copyOwner;
	private boolean copyLocation;
	
	public List<ApiMultiAddEventItem> getItems() {
		return items;
	}
	
	public void setItems(List<ApiMultiAddEventItem> items) {
		this.items = items;
	}
	
	public ApiEvent getEventTemplate() {
		return eventTemplate;
	}

	public void setEventTemplate(ApiEvent eventTemplate) {
		this.eventTemplate = eventTemplate;
	}
	
	public boolean isCopyAssignedTo() {
		return copyAssignedTo;
	}

	public void setCopyAssignedTo(boolean copyAssignedTo) {
		this.copyAssignedTo = copyAssignedTo;
	}

	public boolean isCopyOwner() {
		return copyOwner;
	}
	
	public void setCopyOwner(boolean copyOwner) {
		this.copyOwner = copyOwner;
	}
	
	public boolean isCopyLocation() {
		return copyLocation;
	}

	public void setCopyLocation(boolean copyLocation) {
		this.copyLocation = copyLocation;
	}
}
