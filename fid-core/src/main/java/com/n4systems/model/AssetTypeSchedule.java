package com.n4systems.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.DateHelper;

@Entity
@Table(name = "assettypeschedules")
public class AssetTypeSchedule extends EntityWithOwner implements Saveable, SecurityEnhanced<AssetTypeSchedule> {
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false)
    @JoinColumn(name = "assettype_id")
	private AssetType assetType;
	
	@ManyToOne(optional=false)
    @JoinColumn(name = "inspectiontype_id")
	private EventType eventType;
	

	@Column(name="frequency")
	private Long frequencyInDays;
	private boolean autoSchedule = true;
	
	public AssetTypeSchedule() {}
	
	@AllowSafetyNetworkAccess
	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}
	
	@AllowSafetyNetworkAccess
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Long getFrequency() {
		return frequencyInDays;
	}

	public void setFrequency(Long frequency) {
		this.frequencyInDays = frequency;
	}
	
	public Date getNextDate(Date startDate) {
		return DateHelper.addDaysToDate(startDate, frequencyInDays);
	}
	
	public boolean isAutoSchedule() {
		return autoSchedule;
	}

	public void setAutoSchedule(boolean autoSchedule) {
		this.autoSchedule = autoSchedule;
	}
	
	public boolean isOverride() {
		return !getOwner().isPrimary();
	}

	public AssetTypeSchedule enhance(SecurityLevel level) {
		AssetTypeSchedule enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setAssetType(enhance(assetType, level));
		enhanced.setEventType(enhance(eventType, level));
		return enhanced;
	}
	
	
}
