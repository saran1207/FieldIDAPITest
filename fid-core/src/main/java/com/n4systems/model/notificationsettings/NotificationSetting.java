package com.n4systems.model.notificationsettings;

import com.n4systems.model.*;
import com.n4systems.model.api.HasUser;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.common.RelativeTime;
import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	@ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name="notificationsettings_addresses", joinColumns = @JoinColumn(name="notificationsettings_id"))
	@OrderColumn(name="orderidx")
    private List<String> addresses = new ArrayList<String>();

	private OverdueEventReport overdueReport = new OverdueEventReport();

	private UpcomingEventReport upcomingReport = new UpcomingEventReport();
	
	private FailedEventReport failedReport = new FailedEventReport();
	
	@Column(nullable=false)
	private Boolean sendBlankReport;

	@ManyToOne(optional = true)
	private AssetStatus assetStatus;

	@ManyToOne(optional = true)
    private AssetTypeGroup assetTypeGroup;

	@ManyToOne(optional=true)
	private AssetType assetType;

	@ManyToOne(optional = true)
    private EventTypeGroup eventTypeGroup;

	@ManyToOne(optional=true)
	private EventType eventType;

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
    	return upcomingReport.getPeriodStart();
    }

	public RelativeTime getPeriodEnd() {
    	return upcomingReport.getPeriodEnd();
    }

	@Override
	public User getUser() {
    	return user;
    }

	@Override
	public void setUser(User user) {
    	this.user = user;
    }

	public List<String> getAddresses() {
    	return addresses;
    }

	public void setAddresses(List<String> addresses) {
    	this.addresses = addresses;
    }
    
	public boolean isIncludeOverdue() {
		return overdueReport.includeOverdue;
	}

	public boolean isSmartFailure() {
		return failedReport.smartFailure;
	}
	
	public void setSmartFailure(Boolean smartFailure) {
		failedReport.smartFailure = smartFailure;
	}	
	
	public void setIncludeOverdue(boolean includeOverdue) {
		this.overdueReport.includeOverdue = includeOverdue;
	}

	public UpcomingEventReport getUpcomingReport() {
		return upcomingReport;
	}

	public void setUpcomingReport(UpcomingEventReport upcomingReport) {
		this.upcomingReport = upcomingReport;
	}
	
	public boolean isIncludeFailed(){
		return failedReport.includeFailed;
	}
	
	public void setIncludeFailed(boolean includeFailed) {
		this.failedReport.includeFailed = includeFailed;
	}

	public Boolean getSendBlankReport() {
		return sendBlankReport;
	}

	public void setSendBlankReport(Boolean sendBlankReport) {
		this.sendBlankReport = sendBlankReport;
	}

	public AssetTypeGroup getAssetTypeGroup() {
		return assetTypeGroup;
	}

	public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
		this.assetTypeGroup = assetTypeGroup;
	}

	public AssetStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetStatus assetStatus) {
		this.assetStatus = assetStatus;
	}

	public EventTypeGroup getEventTypeGroup() {
		return eventTypeGroup;
	}

	public void setEventTypeGroup(EventTypeGroup eventTypeGroup) {
		this.eventTypeGroup = eventTypeGroup;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
