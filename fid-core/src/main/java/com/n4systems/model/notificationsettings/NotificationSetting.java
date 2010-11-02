package com.n4systems.model.notificationsettings;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;


import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasUser;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.user.User;

@Entity
@Table(name = "notificationsettings")
public class NotificationSetting extends EntityWithOwner implements HasUser, Saveable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String name;
	
	@Column(name="frequency", nullable=false)
	@Enumerated(EnumType.STRING)
	private SimpleFrequency frequency = SimpleFrequency.DAILY;
	
	
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@Column(name="addr", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
    private List<String> addresses = new ArrayList<String>();

	@Column(name="assettype_id", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<Long> assetTypes = new ArrayList<Long>();
	
	@Column(name="inspectiontype_id", nullable=false)
	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<Long> eventTypes = new ArrayList<Long>();
	
	private OverdueEventReport overdueReport = new OverdueEventReport();

	private UpcomingEventReport upcommingReport = new UpcomingEventReport();

	public NotificationSetting() {}
	
	public NotificationSetting(Tenant tenant, BaseOrg owner) {
		super(tenant, owner);
	}

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public SimpleFrequency getFrequency() {
    	return frequency;
    }

	public void setFrequency(SimpleFrequency frequency) {
    	this.frequency = frequency;
    }

	public RelativeTime getPeriodStart() {
    	return upcommingReport.getPeriodStart();
    }

	
	public RelativeTime getPeriodEnd() {
    	return upcommingReport.getPeriodEnd();
    }

	
	public User getUser() {
    	return user;
    }

	public void setUser(User user) {
    	this.user = user;
    }

	public List<String> getAddresses() {
    	return addresses;
    }

	public void setAddresses(List<String> addresses) {
    	this.addresses = addresses;
    }
    
	public List<Long> getAssetTypes() {
    	return assetTypes;
    }

	public void setAssetTypes(List<Long> assetTypes) {
    	this.assetTypes = assetTypes;
    }

	public List<Long> getEventTypes() {
    	return eventTypes;
    }

	public void setEventTypes(List<Long> eventTypes) {
    	this.eventTypes = eventTypes;
    }

	public boolean isIncludeOverdue() {
		return overdueReport.includeOverdue;
	}

	public void setIncludeOverdue(boolean includeOverdue) {
		this.overdueReport.includeOverdue = includeOverdue;
	}

	public UpcomingEventReport getUpcommingReport() {
		return upcommingReport;
	}

	public void setUpcommingReport(UpcomingEventReport upcommingReport) {
		this.upcommingReport = upcommingReport;
	}
}
