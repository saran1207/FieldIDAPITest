package com.n4systems.model;

import com.n4systems.exceptions.InvalidScheduleStateException;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.DateHelper;
import com.n4systems.util.EnumUtils.LabelledEnumSet;

import javax.persistence.*;
import java.util.Date;
import java.util.EnumSet;

//@Entity
//@Table(name = "eventschedules")
public class EventSchedule extends ArchivableEntityWithOwner implements NetworkEntity<EventSchedule> {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "asset.owner", null, "state");
	}

    public enum ScheduleStatus implements DisplayEnum {
		SCHEDULED("Scheduled"), IN_PROGRESS("In Progress"), COMPLETED("Completed");

		private String label;

		private ScheduleStatus(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public String getName() {
			return name();
		}
	}
    public static LabelledEnumSet ALL_STATUS = new LabelledEnumSet<ScheduleStatus>("All", EnumSet.allOf(ScheduleStatus.class));

	public enum ScheduleStatusGrouping {
		NON_COMPLETE(ScheduleStatus.SCHEDULED, ScheduleStatus.IN_PROGRESS), COMPLETE(ScheduleStatus.COMPLETED);

		private ScheduleStatus[] members;

		private ScheduleStatusGrouping(ScheduleStatus... members) {
			this.members = members;
		}

		public ScheduleStatus[] getMembers() {
			return members;
		}

	}

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name="recurrence_id")
//    private RecurringAssetTypeEvent recurringAssetTypeEvent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="asset_id")
	private Asset asset;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="eventtype_id")
	private EventType eventType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date nextDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;

	@Enumerated(EnumType.STRING)
	private ScheduleStatus status = ScheduleStatus.SCHEDULED;

	@OneToOne(mappedBy="schedule")
	private Event event;

	private Location advancedLocation = new Location();

	@ManyToOne()
	private Project project;
	
	private String mobileGUID;

	public EventSchedule() {
	}

	public EventSchedule(Asset asset, EventType eventType) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public EventSchedule(Asset asset, EventType eventType, Date scheduledDate) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public EventSchedule(Event event) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public EventSchedule(Asset asset, AssetTypeSchedule typeSchedule) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	
	@Override
	protected void onCreate() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	
	@Override
	protected void onUpdate() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	

	@AllowSafetyNetworkAccess
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public EventType getEventType() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
	}

	public void setEventType(EventType eventType) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void setNextDate(Date nextDate) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public Date getNextDate() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public boolean isPastDue() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public static boolean isPastDue(Date nextEventDate) {
		return nextEventDate != null && DateHelper.getToday().after(nextEventDate);
	}

	@AllowSafetyNetworkAccess
	public Long getDaysPastDue() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public Long getDaysToDue() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }


	@AllowSafetyNetworkAccess
	public Date getCompletedDate() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

    public void setCompletedDate(Date date) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public ScheduleStatus getStatus() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void completed(Event event) throws InvalidScheduleStateException {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void inProgress() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void stopProgress() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public Event getEvent() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

    public void setEvent(Event event) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void removeEvent() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public Project getProject() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	public void setProject(Project project) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	
	public EventSchedule enhance(SecurityLevel level) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	
	public String getMobileGUID() {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }
	public void setMobileGUID(String mobileGUID) {
        throw new UnsupportedOperationException("EventSchedules no longer exist.  THis should NOT be called!");
    }

	
	@AllowSafetyNetworkAccess
	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	public void setAdvancedLocation(Location advancedLocation) {
		this.advancedLocation = advancedLocation;
	}

    public Date getNextStandardDate() {
        return nextDate;
    }

    public void setNextStandardDate(Date date) {
        this.nextDate = date;
    }

    public EventSchedule copyDataFrom(Event event) {
        setAsset(event.getAsset());
        setEventType(event.getType());
        setTenant(event.getTenant());
        setCreated(event.getCreated());
        setCreatedBy(event.getCreatedBy());
        setModifiedBy(event.getModifiedBy());
        setModified(event.getModified());

        return this;
    }

    public Date getRelevantDate() {
        if (status == ScheduleStatus.COMPLETED) {
            return getCompletedDate();
        }
        return getNextDate();
    }

    public boolean wasScheduled() {
        return nextDate != null;
    }

}
