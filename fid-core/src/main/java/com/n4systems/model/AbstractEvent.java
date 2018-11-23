package com.n4systems.model;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.util.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class AbstractEvent<T extends EventType, R extends EntityWithTenant> extends EntityWithTenant implements HasFileAttachments {
	private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AbstractEvent.class);

	@Column(length=5000)
	private String comments;

    @ManyToOne(targetEntity = EventType.class)
    @JoinColumn(name="type_id")
    private EventType<T> type;
	
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="eventform_id")
    private EventForm eventForm;

 	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
	@org.hibernate.annotations.Cache(region = "EventCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<CriteriaResult> results = new HashSet<CriteriaResult>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="events_fileattachments", joinColumns = @JoinColumn(name="events_id"), inverseJoinColumns = @JoinColumn(name="attachments_id"))
	@org.hibernate.annotations.Cache(region = "EventCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<FileAttachment> attachments = new ArrayList<FileAttachment>();

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name="events_infooptionmap", joinColumns = @JoinColumn(name="events_id"))
    @MapKeyColumn(name = "mapkey")
    @Column(name="element")
	@org.hibernate.annotations.Cache(region = "EventCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Map<String, String> infoOptionMap = new HashMap<String, String>();

    @Column(name="score")
    private Double score;

    @Column(name="fail_observations")
    private Double failObservations;

    @Column(name="pass_observations")
    private Double passObservations;
	
    @Column(nullable=false)
    private boolean editable = true;

    @Transient
    private List<SectionResults> sectionResults;

	private String mobileGUID;

    @ManyToOne(optional = true)
    @JoinColumn(name="eventstatus_id")
    private EventStatus eventStatus;
	
	@AllowSafetyNetworkAccess
	public String getMobileGUID() {
		return mobileGUID;
	}
	public void setMobileGUID(String mobileGUID) {
		this.mobileGUID = mobileGUID;
	}

	public AbstractEvent() {}

	public AbstractEvent(Tenant tenant) {
		super(tenant);
	}

    // This check may be better refactored into a listener in persistence.xml - NC
	@Override
	protected void onCreate() {
		super.onCreate();
		ensureMobileGuidIsSet();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		ensureMobileGuidIsSet();
	}

	private void ensureMobileGuidIsSet() {
		if(getMobileGUID() == null || getMobileGUID().length() == 0){
			setMobileGUID(UUID.randomUUID().toString());
		}
	}
	
	@Override
    public String toString() {
		String resultString = new String();
		for (CriteriaResult result: getResults()) {
			resultString += "\n" + result;
		}
		
	    return	"id: " + getId() +
	    		"\nTenant: " + getTenant() + 
	    		"\nResults: " + StringUtils.indent(resultString, 1);
    }

	@AllowSafetyNetworkAccess
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@AllowSafetyNetworkAccess
	public Set<CriteriaResult> getResults() {
		return results;
	}

	public void setCriteriaResults(Set<CriteriaResult> results) {
        this.results.clear();
        this.results.addAll(results);
	}

	@Override
	@AllowSafetyNetworkAccess
	public List<FileAttachment> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments(List<FileAttachment> attachments) {
		this.attachments.clear();
        this.attachments.addAll(attachments);
	}
	
	@AllowSafetyNetworkAccess
	public Map<String, String> getInfoOptionMap() {
		return infoOptionMap;
	}

	public void setInfoOptionMap(Map<String, String> infoOptionMap) {
		this.infoOptionMap = infoOptionMap;
	}

	@AllowSafetyNetworkAccess
	public String getComments() {
		return comments;
	}

	public void setComments( String comments ) {

        if (comments != null && !comments.isEmpty()) {
            try {
                int fieldMaxSize = AbstractEvent.class.getDeclaredField("comments").getAnnotation(Column.class).length();
                int fieldSize = comments.length();
                if (fieldSize > fieldMaxSize) {
                    comments = comments.substring(0, fieldMaxSize);
                    logger.warn("AbstractEvent - comments field was truncated to " + comments.length());
                }
            }
            catch (NoSuchFieldException ex) {
                logger.error(ex);
            }
        }
		this.comments = comments;
	}

    @AllowSafetyNetworkAccess
    public EventForm getEventForm() {
        return eventForm;
    }

    public void setEventForm(EventForm eventForm) {
        this.eventForm = eventForm;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getFailObservations() {
        return failObservations;
    }

    public void setFailObservations(Double failObservations) {
        this.failObservations = failObservations;
    }

    public Double getPassObservations() {
        return passObservations;
    }

    public void setPassObservations(Double passObservations) {
        this.passObservations = passObservations;
    }

    @AllowSafetyNetworkAccess
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<FileAttachment> getImageAttachments() {
        //This is way more simple with a stream and theoretically uses less memory.  I'm going to be using this
        //extensively for the emails, so let's try to minimize memory consumption.
        return attachments.stream()
                          .filter(FileAttachment::isImage)
                          .collect(Collectors.toList());
	}

	/**
	 * Finds the CriteriaSections for a given Criteria.
	 * @param criteria	Criteria
	 * @return			The CriteriaSection containing the Criteria or null if no section was found
	 */
	public CriteriaSection findSection(Criteria criteria) {
		CriteriaSection criteriaSection = null;
		if (getEventForm() != null) {
            for(CriteriaSection section: getEventForm().getSections()) {
                if(section.getCriteria().contains(criteria)) {
                    criteriaSection = section;
                    break;
                }
            }
        }
		return criteriaSection;
    }

    public void setTriggersIntoResultingActions(Event triggerEvent) {
        for (CriteriaResult result : getResults()) {
            for (Event action : result.getActions()) {
                copyDataIntoResultingAction(action);
                action.setTriggerEvent(triggerEvent);
                action.setSourceCriteriaResult(result);
            }
        }
    }

    protected abstract void copyDataIntoResultingAction(AbstractEvent<?,?> event);

    public static class SectionResults implements Serializable {
        public List<CriteriaResult> results;
        public CriteriaSection section;
        public Boolean disabled = Boolean.FALSE;
    }

    public List<SectionResults> getSectionResults() {
        return sectionResults;
    }

    public void setSectionResults(List<SectionResults> sectionResults) {
        this.sectionResults = sectionResults;
    }
    
    public boolean containsUnfilledScoreCriteria() {
        for (SectionResults sectionResult : getSectionResults()) {
            if(!sectionResult.disabled) {
                for (CriteriaResult result : sectionResult.results) {
                    if (result.getCriteria().getCriteriaType() == CriteriaType.SCORE && ((ScoreCriteriaResult)result).getScore() == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void storeTransientCriteriaResults() {
        results.clear();
        for (SectionResults sectionResult : getSectionResults()) {
            for (CriteriaResult result : sectionResult.results) {
                if(!sectionResult.disabled) {
                    result.setEvent(this);
                    results.add(result);
                }
            }
        }
    }

    public abstract R getTarget();
    public abstract void setTarget(R target);

    @AllowSafetyNetworkAccess
    public EventType<T> getType() {
        return type;
    }

    public void setType(EventType<T> type) {
        this.type = type;
    }
}
