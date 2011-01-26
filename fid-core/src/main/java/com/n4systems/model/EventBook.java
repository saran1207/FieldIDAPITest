package com.n4systems.model;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "eventbooks")
public class EventBook extends EntityWithOwner implements NamedEntity, Listable<Long>, Comparable<EventBook>, SecurityEnhanced<EventBook> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private boolean open = true;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="book")
	private Set<Event> events = new TreeSet<Event>();

	@Column(nullable=false)
	private String mobileId;
	
	public EventBook() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
		ensureMobileId();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
		ensureMobileId();
	}
	
	private void ensureMobileId() {
		if (mobileId == null) {
			mobileId = UUID.randomUUID().toString();
		}
	}

	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
	}
	
	@Override
    public String toString() {
	    return name + " (" + getId() + ")";
    }

	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isOpen() {
		return open;
	}
	
	@AllowSafetyNetworkAccess
	public Set<Event> getEvents() {
		return events;
	}
	
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	
	@AllowSafetyNetworkAccess
	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	@AllowSafetyNetworkAccess
	public String getDisplayName() {
		String ownerName = (getOwner() != null) ? " (" + getOwner().getName() + ")" : "";
		return getName() + ownerName;
	}

	public int compareTo(EventBook o) {
		if (o == null || getName() == null) return 0;
		
		return getName().compareToIgnoreCase(o.getName());
	}

	public EventBook enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
	
}
