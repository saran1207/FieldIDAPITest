package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.List;

public class ApiMultiAddEvent {
	private List<ApiMultiAddEventItem> items;
	private ApiEvent eventTemplate;
	
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
}
