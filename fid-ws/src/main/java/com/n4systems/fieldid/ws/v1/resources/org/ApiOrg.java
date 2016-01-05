package com.n4systems.fieldid.ws.v1.resources.org;

import com.n4systems.fieldid.ws.v1.resources.eventhistory.ApiPlaceEventHistory;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventType;
import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEvent;

import java.util.List;

public class ApiOrg extends ApiReadonlyModel {
	private String name;
	private String contactName;
	private Long parentId;
	private Long secondaryId;
	private Long customerId;
	private Long divisionId;
	private byte[] image;
	private String address;
    private List<ApiPlaceEventHistory> eventHistory;
    private List<ApiEventType> eventTypes;
    private List<ApiSavedPlaceEvent> events;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSecondaryId() {
		return secondaryId;
	}

	public void setSecondaryId(Long secondaryId) {
		this.secondaryId = secondaryId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

    public List<ApiPlaceEventHistory> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<ApiPlaceEventHistory> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public List<ApiEventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<ApiEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<ApiSavedPlaceEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ApiSavedPlaceEvent> events) {
        this.events = events;
    }
}
