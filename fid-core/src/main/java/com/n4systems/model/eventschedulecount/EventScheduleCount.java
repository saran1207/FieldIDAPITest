package com.n4systems.model.eventschedulecount;

import java.util.Date;

import com.n4systems.model.orgs.BaseOrg;

public class EventScheduleCount implements Comparable<EventScheduleCount> {
	private Date nextEventDate;
	private BaseOrg owner;
	private String assetTypeName;
	private String eventTypeName;
	private long eventCount;
	
	public EventScheduleCount(Date nextEventDate, BaseOrg owner, String assetTypeName, String eventTypeName, long eventCount) {
		this.nextEventDate = nextEventDate;
		this.owner = owner;
		this.assetTypeName = assetTypeName;
		this.eventTypeName = eventTypeName;
		this.eventCount = eventCount;
	}

	public Date getNextEventDate() {
    	return nextEventDate;
    }

	public String getOrganizationName() {
		return (owner != null && owner.getInternalOrg() != null) ? owner.getInternalOrg().getName() : "";
	}
	
	public String getCustomerName() {
		return (owner != null && owner.getCustomerOrg() != null) ? owner.getCustomerOrg().getName() : "";
    }

	public String getDivisionName() {
		return (owner != null && owner.getDivisionOrg() != null) ? owner.getDivisionOrg().getName() : "";
    }

	public String getAssetTypeName() {
    	return assetTypeName;
    }

	public String getEventTypeName() {
    	return eventTypeName;
    }

	public long getEventCount() {
    	return eventCount;
    }

	public int compareTo(EventScheduleCount other) {
		int comp = getNextEventDate().compareTo(other.getNextEventDate());
		
		if (comp == 0) {
			comp = getOrganizationName().compareTo(other.getOrganizationName());
		}
		if (comp == 0) {
			comp = getCustomerName().compareTo(other.getCustomerName());
		}
		if (comp == 0) {
			comp = getDivisionName().compareTo(other.getDivisionName());
		}
		if (comp == 0) {
			comp = getAssetTypeName().compareTo(other.getAssetTypeName());
		}
		if (comp == 0) {
			comp = getEventTypeName().compareTo(other.getEventTypeName());
		}
		
		return comp;
	}
	
	
	
}
