package com.n4systems.model;

import com.n4systems.model.api.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.user.User;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Entity
@Table(name = "masterevents")
@PrimaryKeyJoinColumn(name="event_id")
public class Event extends AbstractEvent implements Comparable<Event>, HasOwner, Archivable, NetworkEntity<Event>, Exportable, LocationContainer {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "asset", "asset.infoOptions", "infoOptionMap", "subEvents" };
	public static final String[] ALL_FIELD_PATHS_WITH_SUB_EVENTS = { "modifiedBy", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "asset", "asset.infoOptions", "infoOptionMap", "subEvents.modifiedBy", "subEvents.eventForm.sections", "subEvents.type.supportedProofTests", "subEvents.type.infoFieldNames", "subEvents.attachments", "subEvents.results", "subEvents.asset.infoOptions", "subEvents.infoOptionMap"};
	
	public static final SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "asset.owner", null, "state");
	}

    public enum EventState implements DisplayEnum {
        OPEN("Open"), COMPLETED("Completed"), CLOSED("Closed");

        private String label;

        private EventState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }

    public enum EventStateGrouping {
        NON_COMPLETE(EventState.OPEN, EventState.CLOSED), COMPLETE(EventState.COMPLETED);

        private EventState[] members;

        private EventStateGrouping(EventState... members) {
            this.members = members;
        }

        public EventState[] getMembers() {
            return members;
        }

    }

	private Location advancedLocation = new Location();

    @OneToOne(mappedBy = "event", cascade = CascadeType.PERSIST)
    private AssigneeNotification assigneeNotification;

	@Column(nullable=false)
	private boolean printable;

	@ManyToOne(fetch=FetchType.EAGER)
	private User performedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date nextDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;

	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	private EventGroup group;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private EventBook book;

    @ManyToOne
    @JoinColumn(name="assignee_id")
    private User assignee;

    @ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="owner_id", nullable = false)
	private BaseOrg owner;
	
	private ProofTestInfo proofTestInfo;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@IndexColumn(name="orderidx")
    @JoinTable(name = "masterevents_subevents", joinColumns = @JoinColumn(name="masterevents_event_id"), inverseJoinColumns = @JoinColumn(name="subevents_event_id"))
	private List<SubEvent> subEvents = new ArrayList<SubEvent>();
	
	@Enumerated(EnumType.STRING)
	@Column
	private Status status = Status.NA;

    @Enumerated(EnumType.STRING)
    @Column(name="event_state", nullable=false)
    private EventState eventState = EventState.OPEN;
		
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="schedule_id")
    @Deprecated
    // Pertinent information isn't stored in the schedule anymore, it lives in the event.
	private EventSchedule schedule = new EventSchedule();

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
	
	@Embedded 
	private GpsLocation gpsLocation = new GpsLocation();
	
	private AssignedToUpdate assignedTo;

    @Transient
    private boolean resultFromCriteriaAvailable = false;

    @ManyToOne()
    @JoinColumn(name="recurring_event_id")
    private RecurringAssetTypeEvent recurringEvent;
	
	public Event() {
	}

	public Event(Tenant tenant) {
		super(tenant);
	}

	@AllowSafetyNetworkAccess
	public Date getDate() {
		return getCompletedDate();
	}
	
	@AllowSafetyNetworkAccess
	public Date getDateInUserTime(TimeZone timeZone) {
		return DateHelper.convertToUserTimeZone(getDate(), timeZone);
	}

	public void setDate(Date date) {
		setCompletedDate(date);
	}

	@AllowSafetyNetworkAccess
	public boolean isPrintable() {
		return printable;
	}

	public void setPrintable(boolean printable) {
		this.printable = printable;
	}

	@AllowSafetyNetworkAccess
	public User getPerformedBy() {
		return performedBy;
	}
	

	public void setPerformedBy(User performedBy) {
		this.performedBy = performedBy;
	}

	@AllowSafetyNetworkAccess
	public EventGroup getGroup() {
		return group;
	}
	

	public void setGroup(EventGroup group) {
		this.group = group;
	}

	public EventBook getBook() {
		return book;
	}

	public void setBook(EventBook book) {
		this.book = book;
	}

	@Override
	@AllowSafetyNetworkAccess
	public BaseOrg getOwner() {
		return owner;
	}
	
	@Override
	public void setOwner(BaseOrg owner) {
		this.owner = owner;
	}

	@AllowSafetyNetworkAccess
	public ProofTestInfo getProofTestInfo() {
		return proofTestInfo;
	}

	public void setProofTestInfo(ProofTestInfo proofTestInfo) {
		this.proofTestInfo = proofTestInfo;
	}

	@AllowSafetyNetworkAccess
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public int compareTo( Event event) {
		if( event == null ) {
			return -1;
		}
		int compare = getDate().compareTo( event.getDate() );
		
		return ( compare == 0 ) ? getCreated().compareTo( event.getCreated() ) : compare;
	}

	@AllowSafetyNetworkAccess
	public List<SubEvent> getSubEvents() {
		return subEvents;
	}

	public void setSubEvents(List<SubEvent> subEvents) {
		this.subEvents = subEvents;
	}

	@Override
	public void activateEntity() {
		state = EntityState.ACTIVE;
	}

	@Override
	public void archiveEntity() {
		state = EntityState.ARCHIVED;
	}

	@Override
	@AllowSafetyNetworkAccess
	public EntityState getEntityState() {
		return state;
	}

	@Override
	public void retireEntity() {
		state = EntityState.RETIRED;
	}
	
	@Override
	public void setRetired( boolean retired ) {
		if( retired ) {
			retireEntity();
		} else  {
			activateEntity();
		}
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public boolean isRetired() {
		return state == EntityState.RETIRED;
	}

	@Override
	@AllowSafetyNetworkAccess
	public boolean isActive() {
		return state == EntityState.ACTIVE;
	}
	
	@Override
	@AllowSafetyNetworkAccess
	public boolean isArchived() {
		return state == EntityState.ARCHIVED;
	}
	
	@AllowSafetyNetworkAccess
	public boolean isPrintableForReportType(EventReportType reportType) {
		if (!printable) {
			return false;
		}
		
		PrintOut printOut = getType().getGroup().getPrintOutForReportType(reportType);
		return (printOut != null);
	}

	@AllowSafetyNetworkAccess
	public boolean isEventCertPrintable() {
		return isPrintableForReportType(EventReportType.INSPECTION_CERT);
	}

	@AllowSafetyNetworkAccess
	public boolean isObservationCertPrintable() { 
		return isPrintableForReportType(EventReportType.OBSERVATION_CERT);
	}
	
	@AllowSafetyNetworkAccess
	public boolean isAnyCertPrintable() {
		boolean isAnyPrintable = false;
		for (EventReportType reportType: EventReportType.values()) {
			if (isPrintableForReportType(reportType)) {
				isAnyPrintable = true;
				break;
			}
		}
		return isAnyPrintable;
	}
	
	@Override
    public String toString() {
		String subEventString = new String();
		for (SubEvent subEvent : getSubEvents()) {
			subEventString += "\n" + subEvent;
		}
		
	    return	super.toString() +
	    		"\nAssigned To: " + assignedTo +
	    		"\nState: " + state + 
	    		"\nDate: " + getDate() +
	    		"\nOwner: " + getOwner() +
	    		"\nBook: " + getBook() +
	    		"\nPerformed By: " + getPerformedBy() + 
	    		"\nStatus: " + getStatus() + 
	    		"\nSubEvents: " + StringUtils.indent(subEventString, 1);
    }

	@AllowSafetyNetworkAccess
    @Deprecated
	public EventSchedule getSchedule() {
		return schedule;
	}

    @Deprecated
    public void setSchedule(EventSchedule schedule) {
        this.schedule = schedule;
    }

	@Override
	@AllowSafetyNetworkAccess
	public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
		return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
	}
	
	@Override
	public Event enhance(SecurityLevel level) {
		Event enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setBook(enhance(book, level));
		enhanced.setPerformedBy(enhance(performedBy, level));
		enhanced.setGroup(enhance(group, level));
		enhanced.setType(enhance(getType(), level));
		enhanced.setAsset(enhance(getAsset(), level));
		enhanced.setOwner(enhance(getOwner(), level));
		
		List<SubEvent> enhancedSubEvents = new ArrayList<SubEvent>();
		for (SubEvent subEvent : getSubEvents()) {
			enhancedSubEvents.add(subEvent.enhance(level));
		}
		enhanced.setSubEvents(enhancedSubEvents);
		
		return enhanced;
	}

	// Events are never exported
	@Override
	public String getGlobalId() {
		return null;
	}

	@Override
	public void setGlobalId(String globalId) {}

	public AssignedToUpdate getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(AssignedToUpdate assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public void removeAssignTo() {
		assignedTo = null;
	}
	
	public boolean hasAssignToUpdate() {
		return assignedTo != null && assignedTo.isAssignmentApplyed();
	}

	@Override
	protected void onCreate() {
		super.onCreate();
        assigneeNotification = new AssigneeNotification();
        assigneeNotification.setEvent(this);
		normalizeAssignmentForPersistence();
        fillInPlaceholderScheduleIfAbsent();
	}

    private void fillInPlaceholderScheduleIfAbsent() {
        if (schedule.getId() == null) {
            schedule.copyDataFrom(this);
        }
    }

    private void normalizeAssignmentForPersistence() {
		if (assignedTo == null)
			assignedTo = AssignedToUpdate.ignoreAssignment();
	}
	

	@Override
	protected void onUpdate() {
		super.onUpdate();
		normalizeAssignmentForPersistence();
	}

	@Override
	@AllowSafetyNetworkAccess
	public Location getAdvancedLocation() {
		return advancedLocation;
	}

	@Override
	public void setAdvancedLocation(Location advancedLocation) {
		if (advancedLocation == null) {
			advancedLocation = new Location();
		}
		this.advancedLocation = advancedLocation;
	}
	
	public GpsLocation getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(GpsLocation gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

    public boolean isResultFromCriteriaAvailable() {
        return resultFromCriteriaAvailable;
    }

    public void setResultFromCriteriaAvailable(boolean resultFromCriteriaAvailable) {
        this.resultFromCriteriaAvailable = resultFromCriteriaAvailable;
    }
    
    public void setInitialResultBasedOnScoreOrOneClicksBeingAvailable() {
        resultFromCriteriaAvailable = false;
        if (getType().getEventForm() != null) {
            if (getType().getEventForm().isUseScoreForResult()) {
                resultFromCriteriaAvailable = true;
            } else {
                for (CriteriaSection criteriaSection : getType().getEventForm().getAvailableSections()) {
                    for (Criteria criteria : criteriaSection.getAvailableCriteria()) {
                        if (criteria.getCriteriaType() == CriteriaType.ONE_CLICK) {
                            resultFromCriteriaAvailable = true;
                            break;
                        }
                    }
                }
            }
        }

        if (resultFromCriteriaAvailable) {
            status = null;
        } else {
            status = Status.NA;
        }
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    public Date getRelevantDate() {
        if (getEventState() == EventState.COMPLETED) {
            return getCompletedDate();
        }
        return getNextDate();
    }

    @AllowSafetyNetworkAccess
    public Long getDaysPastDue() {
        Long daysPast = null;
        if (isPastDue()) {
            daysPast = DateHelper.getDaysUntilToday(nextDate);
        }
        return daysPast;
    }

    @AllowSafetyNetworkAccess
    public Long getDaysToDue() {
        Long daysTo = null;
        if (!isPastDue()) {
            daysTo = DateHelper.getDaysFromToday(nextDate);
        }
        return daysTo;
    }

    @AllowSafetyNetworkAccess
    public boolean isPastDue() {
        return (getEventState() == EventState.OPEN && isPastDue(nextDate));
    }

    public boolean isCompleted() {
        return EventState.COMPLETED.equals(getEventState());
    }

    /**
     * A static method consolidating the logic for checking if a next inspection
     * date is past due.
     *
     * @param nextEventDate
     *            The next event date
     * @return True if nextEventDate is after {@link DateHelper#getToday()
     *         today}
     */
    @AllowSafetyNetworkAccess
    public static boolean isPastDue(Date nextEventDate) {
        return nextEventDate != null && DateHelper.getToday().after(nextEventDate);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean wasScheduled() {
        return nextDate != null;
    }

    public EventType getEventType() {
        return getType();
    }

    public RecurringAssetTypeEvent getRecurringEvent() {
        return recurringEvent;
    }

    public void setRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        this.recurringEvent = recurringEvent;
    }

    public Event copyDataFrom(Event event) {
        setAsset(event.getAsset());
        setType(event.getType());
        setTenant(event.getTenant());
        setCreated(event.getCreated());
        setCreatedBy(event.getCreatedBy());
        setModifiedBy(event.getModifiedBy());
        setModified(event.getModified());

        return this;
    }

    public EntityState getState() {
        return state;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public AssigneeNotification getAssigneeNotification() {
        return assigneeNotification;
    }

    public void setAssigneeNotification(AssigneeNotification assigneeNotification) {
        this.assigneeNotification = assigneeNotification;
    }
}
