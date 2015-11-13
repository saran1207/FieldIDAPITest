package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import java.util.Date;
import java.util.List;

public class ApiEventHistory {
	private String assetId;
	private String eventId;
	private Long eventTypeId;
	private String eventTypeName;
	private Long assetTypeId;
	private Date eventDate;
	private String performedBy;
	private String status;
	private boolean printable;
    private List<ApiEventHistory> subEvents;

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

    public List<ApiEventHistory> getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(List<ApiEventHistory> subEvents) {
        this.subEvents = subEvents;
    }
}
