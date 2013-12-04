package com.n4systems.model;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractEvent<T extends EventType, R extends EntityWithTenant> extends EntityWithTenant implements HasFileAttachments {
	private static final long serialVersionUID = 1L;

	@Column(length=2500)
	private String comments;
	
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="eventform_id")
    private EventForm eventForm;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "event", cascade=CascadeType.ALL)
	private Set<CriteriaResult> results = new HashSet<CriteriaResult>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<FileAttachment> attachments = new ArrayList<FileAttachment>();

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name="events_infooptionmap", joinColumns = @JoinColumn(name="events_id"))
    @MapKeyColumn(name = "mapkey")
    @Column(name="element")
    private Map<String, String> infoOptionMap = new HashMap<String, String>();

    @Column(name="score")
    private Double score;
	
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
		if (mobileGUID == null) {
			mobileGUID = UUID.randomUUID().toString();
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
		this.results = results;
	}

	@Override
	@AllowSafetyNetworkAccess
	public List<FileAttachment> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments(List<FileAttachment> attachments) {
		this.attachments = attachments;
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
		this.comments = comments;
	}

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

    @AllowSafetyNetworkAccess
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public List<FileAttachment> getImageAttachments() {
		List<FileAttachment> imageAttachments = new ArrayList<FileAttachment>();
		for (FileAttachment fileAttachment : attachments) {
			if (fileAttachment.isImage()) {
				imageAttachments.add(fileAttachment);
			}
		}
		return imageAttachments;
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
    }

    public List<SectionResults> getSectionResults() {
        return sectionResults;
    }

    public void setSectionResults(List<SectionResults> sectionResults) {
        this.sectionResults = sectionResults;
    }
    
    public boolean containsUnfilledScoreCriteria() {
        for (SectionResults sectionResult : getSectionResults()) {
            for (CriteriaResult result : sectionResult.results) {
                if (result.getCriteria().getCriteriaType() == CriteriaType.SCORE && ((ScoreCriteriaResult)result).getScore() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void storeTransientCriteriaResults() {
        results.clear();
        for (SectionResults sectionResult : getSectionResults()) {
            for (CriteriaResult result : sectionResult.results) {
                result.setEvent(this);
                results.add(result);
            }
        }
    }

    public abstract T getType();
    public abstract void setType(T type);

    public abstract R getTarget();
    public abstract void setTarget(R target);

}
