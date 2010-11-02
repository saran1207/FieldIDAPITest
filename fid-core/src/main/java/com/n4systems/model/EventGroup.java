package com.n4systems.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "inspectiongroups")
public class EventGroup extends EntityWithTenant implements SecurityEnhanced<EventGroup> {
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "group")
	private Set<Event> events = new TreeSet<Event>();

	private String mobileGuid;

	public EventGroup() {
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@AllowSafetyNetworkAccess
	public Set<Event> getEvents() {
		return events;
	}

	@AllowSafetyNetworkAccess
	public String getMobileGuid() {
		return mobileGuid;
	}

	public void setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
	}

	@AllowSafetyNetworkAccess
	public Date getFirstDate() {
		Date minDate = null;
		for (Event event : getAvailableInspections()) {
			if (minDate == null) {
				minDate = event.getDate();
			} else if (event.getDate().before(minDate)) {
				minDate = event.getDate();
			}
		}

		return minDate;
	}

	@AllowSafetyNetworkAccess
	public Date getLastDate() {
		Date maxDate = null;
		for (Event event : getAvailableInspections()) {
			if (maxDate == null) {
				maxDate = event.getDate();
			} else if (event.getDate().after(maxDate)) {
				maxDate = event.getDate();
			}
		}

		return maxDate;
	}

	@AllowSafetyNetworkAccess
	public List<Event> getAvailableInspections() {
		List<Event> availableEvents = new ArrayList<Event>();

		for (Event event : events) {
			if (event.isActive()) {
				availableEvents.add(event);
			}

		}
		return availableEvents;
	}

	public EventGroup enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
}
