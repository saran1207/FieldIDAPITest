package com.n4systems.fieldid.ws.v1.resources.eventattachment;

import java.util.List;

public class ApiMultiEventAttachment extends ApiEventAttachment{
	private List<String> events;
	
	public List<String> getEvents() {
		return events;
	}	

	public void setEvents(List<String> events) {
		this.events = events;
	}	
}
