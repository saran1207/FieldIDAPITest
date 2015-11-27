package com.n4systems.model;

import com.n4systems.model.api.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.utils.ActionDescriptionUtil;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.DateHelper;
import com.n4systems.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Entity
@Table(name = "masterevents")
@PrimaryKeyJoinColumn(name="event_id")
public abstract class Event<T extends EventType, V extends Event, R extends EntityWithTenant> extends AbstractEvent<T,R> implements Comparable<Event>, Archivable, Exportable, LocationContainer, HasCreatedModifiedPlatform, HasOwner, HasGpsLocation {
	private static final long serialVersionUID = 1L;
    public static final String[] PROCEDURE_AUDIT_FIELD_PATHS = { "modifiedBy", "createdBy", "eventForm.sections", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "infoOptionMap", "subEvents", "procedureDefinition" };
    public static final String[] PLACE_FIELD_PATHS = { "modifiedBy", "createdBy", "eventForm.sections", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "infoOptionMap", "subEvents", "place" };
    public static final String[] ALL_FIELD_PATHS = { "modifiedBy", "createdBy", "eventForm.sections", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "asset", "asset.infoOptions", "infoOptionMap", "subEvents" };
	public static final String[] ALL_FIELD_PATHS_WITH_SUB_EVENTS = { "modifiedBy", "createdBy", "eventForm.sections", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "asset", "asset.infoOptions", "infoOptionMap", "subEvents.eventForm.sections"};
    public static final String[] THING_TYPE_PATHS = { "type.supportedProofTests" };

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

    @ManyToOne
    @JoinColumn(name="assigned_group_id")
    private UserGroup assignedGroup;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@OrderColumn(name="orderidx")
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

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
	
	@Embedded 
	private GpsLocation gpsLocation = new GpsLocation();
	
	private AssignedToUpdate assignedTo;

    @Transient
    private boolean resultFromCriteriaAvailable = false;

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

    @Column(name="send_email_on_update")
    private Boolean sendEmailOnUpdate = Boolean.TRUE;

    @Transient
    private Boolean assigneeOrDateUpdated = Boolean.FALSE;

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
//		this.subEvents.clear();
//		this.subEvents.addAll(subEvents);
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
	    		"\nBook: " + getBook() +
	    		"\nPerformed By: " + getPerformedBy() + 
	    		"\nResult: " + getEventResult() +
	    		"\nSubEvents: " + StringUtils.indent(subEventString, 1);
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
		if(sendEmailOnUpdate) {
			assigneeNotification = new AssigneeNotification();
			assigneeNotification.setEvent(this);
			setAssigneeNotification(assigneeNotification);
		}
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

    public void setResultFromCriteriaAvailable() {
        resultFromCriteriaAvailable = false;
        if (getType().getEventForm() != null) {
            if (getType().getEventForm().isUseScoreForResult()) {
                resultFromCriteriaAvailable = true;
            } else if (getType().getEventForm().isUseObservationCountForResult()) {
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
    }
    
    public void setInitialResultBasedOnScoreOrOneClicksBeingAvailable() {
        setResultFromCriteriaAvailable();

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

    public EntityState getState() {
        return state;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignedGroup = null;
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

    public UserGroup getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(UserGroup assignedGroup) {
        this.assignee = null;
        this.assignedGroup = assignedGroup;
    }

    public Boolean isSendEmailOnUpdate() {
        return sendEmailOnUpdate;
    }

    public void setSendEmailOnUpdate(Boolean sendEmailOnUpdate) {
        this.sendEmailOnUpdate = sendEmailOnUpdate;
    }

    @Transient
    public String getAssigneeName() {
        // Used as a path_expression in reporting for column event_search_assignee
        if (assignedGroup != null) {
            return assignedGroup.getName();
        } else if (assignee != null) {
            return assignee.getFullName();
        }
        return null;
    }

    @Transient
    public Assignable getAssignedUserOrGroup() {
        return assignee != null ?  assignee : assignedGroup;
    }

    public void setAssignedUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setAssignee((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setAssignedGroup((UserGroup) assignee);
        } else if (assignee == null) {
            this.assignee = null;
            this.assignedGroup = null;
        }
    }

    public Boolean getAssigneeOrDateUpdated() {
        return assigneeOrDateUpdated;
    }

    public void setAssigneeOrDateUpdated() {
        this.assigneeOrDateUpdated = Boolean.TRUE;
    }
}

