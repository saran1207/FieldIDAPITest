package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name="associatedeventtypes")
public class AssociatedEventType extends EntityWithTenant implements Saveable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name="inspectiontype_id")
	private EventType eventType;
	
	@ManyToOne
    @JoinColumn(name="producttype_id")
	private AssetType assetType;
	
	public AssociatedEventType() {
	}
	
	public AssociatedEventType(EventType eventType, AssetType assetType) {
		super(eventType.getTenant());
		this.eventType = eventType;
		this.assetType = assetType;
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
