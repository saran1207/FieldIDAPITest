package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.event.criteria.ApiMultiEventCriteriaResultItem;

public class ApiMultiAddEventItem {
	private String eventId;
	private String assetId;
	private boolean scheduled;
	private List<ApiMultiEventCriteriaResultItem> results;
	
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
	
	public List<ApiMultiEventCriteriaResultItem> getResults() {
		return results;
	}

	public void setResults(List<ApiMultiEventCriteriaResultItem> results) {
		this.results = results;
	}	
}
