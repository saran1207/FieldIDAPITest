package com.n4systems.fieldid.ws.v1.resources.eventschedule;

import java.util.Date;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

public class ApiEventSchedule extends ApiReadWriteModelWithOwner {
	private String assetId;
	private Long eventTypeId;
	private Date nextDate;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

}
