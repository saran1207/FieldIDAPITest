package com.n4systems.fieldid.ws.v1.resources.eventattachment;

import java.util.List;

public class ApiMultiEventAttachment{
	private ApiEventAttachment eventAttachmentTemplate;
	private List<String> eventIds;	
	
	public ApiEventAttachment getEventAttachmentTemplate() {
		return eventAttachmentTemplate;
	}

	public void setEventAttachmentTemplate(ApiEventAttachment eventAttachmentTemplate) {
		this.eventAttachmentTemplate = eventAttachmentTemplate;
	}

	public List<String> getEventIds() {
		return eventIds;
	}	

	public void setEventIds(List<String> eventIds) {
		this.eventIds = eventIds;
	}
}
