package com.n4systems.model.eventschedulecount;

import com.n4systems.model.orgs.BaseOrg;

import java.util.Date;

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

        //Because of some weird quirk with how tests are being run against this class under Java 8, this
        //ridiculous "if" structure needed to be added.  In any real world situation, this would never actually
        //happen, because of the fact that these fields are not nullable on the JPA entity.  This is just due
        //to a peculiarity with EasyMock that was NOT addressed by the shift to cglib:cglib-nodep:3.2
		if (comp == 0) {
            if(getAssetTypeName() == null && other.getAssetTypeName() ==  null) {
                comp = 0;
            } else {
                if(getAssetTypeName() == null && other.getAssetTypeName() != null) {
                    comp = -1;
                } else {
                    if(getAssetTypeName() != null && other.getAssetTypeName() == null) {
                        comp = 1;
                    } else {
                        comp = getAssetTypeName().compareTo(other.getAssetTypeName());
                    }
                }
            }
		}
		if (comp == 0) {
            if(getEventTypeName() == null && other.getEventTypeName() == null) {
                comp = 0;
            } else {
                if(getEventTypeName() == null && other.getEventTypeName() != null) {
                    comp = -1;
                } else {
                    if(getEventTypeName() != null && other.getEventTypeName() == null) {
                        comp = 1;
                    } else {
                        comp = getEventTypeName().compareTo(other.getEventTypeName());
                    }
                }
            }
		}
		
		return comp;
	}
	
	
	
}
