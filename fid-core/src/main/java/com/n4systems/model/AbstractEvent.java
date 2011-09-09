package com.n4systems.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "events")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractEvent extends EntityWithTenant implements HasFileAttachments {
	private static final long serialVersionUID = 1L;

	@Column( length=2500 )
	private String comments;
	
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	private EventType type;

    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name="eventform_id")
    private EventForm eventForm;

	@ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name="asset_id")
	private Asset asset;
	
	@ManyToOne(optional = true)
	@JoinColumn(name="assetstatus_id")
	private AssetStatus assetStatus;
	
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
	
	private String mobileGUID;
	
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
	    		"\nType: " + getType() +
	    		"\nAsset: " + getAsset() +
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
	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@AllowSafetyNetworkAccess
	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	@AllowSafetyNetworkAccess
	public AssetStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetStatus assetStatus) {
		this.assetStatus = assetStatus;
	}

	@AllowSafetyNetworkAccess
	public Set<CriteriaResult> getResults() {
		return results;
	}

	public void setCriteriaResults(Set<CriteriaResult> results) {
		this.results = results;
	}

	@AllowSafetyNetworkAccess
	public List<FileAttachment> getAttachments() {
		return attachments;
	}

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

}
