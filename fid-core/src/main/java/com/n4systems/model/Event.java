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
import com.n4systems.model.utils.ActionDescriptionUtil;
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
public class Event extends AbstractEvent implements Comparable<Event>, HasOwner, Archivable, NetworkEntity<Event>, Exportable, LocationContainer, HasCreatedModifiedPlatform {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "asset", "asset.infoOptions", "infoOptionMap", "subEvents" };
	public static final String[] ALL_FIELD_PATHS_WITH_SUB_EVENTS = { "modifiedBy", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "asset", "asset.infoOptions", "infoOptionMap", "subEvents.modifiedBy", "subEvents.eventForm.sections", "subEvents.type.supportedProofTests", "subEvents.type.infoFieldNames", "subEvents.attachments", "subEvents.results", "subEvents.asset.infoOptions", "subEvents.infoOptionMap"};
	
	public static final SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenant.id", "asset.owner", null, "state");
	}

    public enum WorkflowState implements DisplayEnum {
        OPEN("Open"), COMPLETED("Completed"), CLOSED("Closed");

        private String label;

        private WorkflowState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }

    public enum WorkflowStateGrouping {
        NON_COMPLETE(WorkflowState.OPEN, WorkflowState.CLOSED), COMPLETE(WorkflowState.COMPLETED);

        private WorkflowState[] members;

        private WorkflowStateGrouping(WorkflowState... members) {
            this.members = members;
        }

        public WorkflowState[] getMembers() {
            return members;
        }

    }

	private Location advancedLocation = new Location();

    @OneToOne(mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private AssigneeNotification assigneeNotification;

    // Trigger and criteria result.
    // Theoretically we can get the trigger event from the sourceCriteriaResult, but this will
    // almost certainly present significant performance issues for reporting if we choose to ONLY
    // have the link to criteria result (we'll have to join in criteriaresults with events to do reports)
    // For now, let's have both the trigger event and source criteria result linked from events.
    @OneToOne
    @JoinColumn(name="trigger_event_id")
    private Event triggerEvent;

    @ManyToOne
    @JoinColumn(name="source_criteria_result_id")
    private CriteriaResult sourceCriteriaResult;

    @Column(nullable=false)
	private boolean printable;

	@ManyToOne(fetch=FetchType.EAGER)
	private User performedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedDate;
	
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
	@Column(name="event_result", nullable = false)
	private EventResult eventResult = EventResult.NA;

    @Enumerated(EnumType.STRING)
    @Column(name="workflow_state", nullable=false)
    private WorkflowState workflowState = WorkflowState.OPEN;
		
	@Enumerated(EnumType.STRING)
	private EntityState state = EntityState.ACTIVE;
	
    @Deprecated
    @Column(name="schedule_id")
    private Long scheduleId;

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

    @Column(name="notes", length = 500)
    private String notes;

    @ManyToOne
    @JoinColumn(name="priority_id")
    private PriorityCode priority;

    @Enumerated(EnumType.STRING)
    @Column(name="modified_platform_type")
    private PlatformType modifiedPlatformType;

    @Column(name="modified_platform", length = 200)
    private String modifiedPlatform;

    @Enumerated(EnumType.STRING)
    @Column(name="created_platform_type")
    private PlatformType createdPlatformType;

    @Column(name="created_platform", length = 200)
    private String createdPlatform;

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
	public EventResult getEventResult() {
		return eventResult;
	}

	public void setEventResult(EventResult eventResult) {
		this.eventResult = eventResult;
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
	    		"\nResult: " + getEventResult() +
	    		"\nSubEvents: " + StringUtils.indent(subEventString, 1);
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
        setTriggersIntoResultingActions(this);
	}

    private void normalizeAssignmentForPersistence() {
		if (assignedTo == null)
			assignedTo = AssignedToUpdate.ignoreAssignment();
	}
	

	@Override
	protected void onUpdate() {
		super.onUpdate();
		normalizeAssignmentForPersistence();
        setTriggersIntoResultingActions(this);
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
                        if (criteria.getCriteriaType() == CriteriaType.ONE_CLICK && ((OneClickCriteria)criteria).isPrincipal()) {
                            resultFromCriteriaAvailable = true;
                            break;
                        }
                    }
                }
            }
        }

        if (resultFromCriteriaAvailable) {
            eventResult = EventResult.VOID;
        } else {
            eventResult = EventResult.PASS;
        }
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    @AllowSafetyNetworkAccess
    public WorkflowState getWorkflowState() {
        return workflowState;
    }

    public void setWorkflowState(WorkflowState workflowState) {
        this.workflowState = workflowState;
    }

    public Date getRelevantDate() {
        if (getWorkflowState() == WorkflowState.COMPLETED) {
            return getCompletedDate();
        }
        return getDueDate();
    }

    @AllowSafetyNetworkAccess
    @Deprecated //Use DateService to calculate
    public Long getDaysPastDue() {
        Long daysPast = null;
        if (isPastDue()) {
            daysPast = DateHelper.getDaysUntilToday(dueDate);
        }
        return daysPast;
    }

    @AllowSafetyNetworkAccess
    @Deprecated //Use DateService to calculate
    public Long getDaysToDue() {
        Long daysTo = null;
        if (!isPastDue()) {
            daysTo = DateHelper.getDaysFromToday(dueDate);
        }
        return daysTo;
    }

    @AllowSafetyNetworkAccess
    @Deprecated //DateHelper.getToday does not take into account the user timezone use DateService
    public boolean isPastDue() {
        return (getWorkflowState() == WorkflowState.OPEN && isPastDue(dueDate));
    }

    public boolean isCompleted() {
        return WorkflowState.COMPLETED.equals(getWorkflowState());
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
    @Deprecated //DateHelper.getToday does not take into account the user timezone use DateService
    private boolean isPastDue(Date nextEventDate) {
        return nextEventDate != null && DateHelper.getToday().after(nextEventDate);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean wasScheduled() {
        return dueDate != null;
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

    public boolean isAssigned() {
        return getAssignee()!=null;
    }

    public Event getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(Event triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PriorityCode getPriority() {
        return priority;
    }

    public void setPriority(PriorityCode priority) {
        this.priority = priority;
    }

    public CriteriaResult getSourceCriteriaResult() {
        return sourceCriteriaResult;
    }

    public void setSourceCriteriaResult(CriteriaResult sourceCriteriaResult) {
        this.sourceCriteriaResult = sourceCriteriaResult;
    }

    public String getActionDescription() {
        return ActionDescriptionUtil.getDescription(triggerEvent, sourceCriteriaResult);
    }

    public boolean isAction() {
        return getTriggerEvent() != null;
    }

    @Deprecated
    public Long getScheduleId() {
        return scheduleId;
    }

    @Deprecated
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getModifiedPlatform() {
        return modifiedPlatform;
    }

    public void setModifiedPlatform(String modifiedPlatform) {
        this.modifiedPlatform = modifiedPlatform;
    }

    public String getCreatedPlatform() {
        return createdPlatform;
    }

    public void setCreatedPlatform(String createdPlatform) {
        this.createdPlatform = createdPlatform;
    }

    public PlatformType getModifiedPlatformType() {
        return modifiedPlatformType;
    }

    public void setModifiedPlatformType(PlatformType modifiedPlatformType) {
        this.modifiedPlatformType = modifiedPlatformType;
    }

    public PlatformType getCreatedPlatformType() {
        return createdPlatformType;
    }

    public void setCreatedPlatformType(PlatformType createdPlatformType) {
        this.createdPlatformType = createdPlatformType;
    }
}
