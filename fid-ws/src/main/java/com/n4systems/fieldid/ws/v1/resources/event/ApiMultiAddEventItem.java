package com.n4systems.fieldid.ws.v1.resources.event;

public class ApiMultiAddEventItem {
	private String assetId;
	private String eventScheduleId;
	
	public String getAssetId() {
		return assetId;
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}	

	public String getEventScheduleId() {
		return eventScheduleId;
	}	
	
	public void setEventScheduleId(String eventScheduleId) {
		this.eventScheduleId = eventScheduleId;
	}
}
