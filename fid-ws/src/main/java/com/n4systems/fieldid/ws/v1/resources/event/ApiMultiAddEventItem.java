package com.n4systems.fieldid.ws.v1.resources.event;

public class ApiMultiAddEventItem {
	private String eventId;
	private String assetId;
	private boolean scheduled;
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public String getAssetId() {
		return assetId;
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}	
}
