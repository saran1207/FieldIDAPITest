package com.n4systems.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.n4systems.model.security.AllowSafetyNetworkAccess;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.api.HasFileAttachments;
import com.n4systems.model.parents.EntityWithTenant;
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
	
	@Column(nullable=false)
	private long formVersion;
	
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
    public String toString() {
		String resultString = new String();
		for (CriteriaResult result: getResults()) {
			resultString += "\n" + result;
		}
		
	    return	"id: " + getId() +
	    		"\nTenant: " + getTenant() + 
	    		"\nType: " + getType() +
	    		"\nForm Ver: " + getFormVersion() +
	    		"\nAsset: " + getAsset() +
	    		"\nResults: " + StringUtils.indent(resultString, 1);
    }

	@AllowSafetyNetworkAccess
	public boolean isEditable() {
		return (formVersion == type.getFormVersion());
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

	public void setResults(Set<CriteriaResult> results) {
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

	@AllowSafetyNetworkAccess
	public long getFormVersion() {
    	return formVersion;
    }

	public void setFormVersion(long formVersion) {
    	this.formVersion = formVersion;
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
	public void syncFormVersionWithType() {
		setFormVersion(getType().getFormVersion());
	}
	
}
