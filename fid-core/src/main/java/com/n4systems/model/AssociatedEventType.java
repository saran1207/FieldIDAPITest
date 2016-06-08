package com.n4systems.model;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="associated_event_types")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AssociatedEventType extends EntityWithTenant implements Saveable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name="thing_event_type_id")
	private ThingEventType eventType;
	
	@ManyToOne
    @JoinColumn(name="asset_type_id")
	private AssetType assetType;
	
	public AssociatedEventType() {
	}
	
	public AssociatedEventType(ThingEventType eventType, AssetType assetType) {
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

	public ThingEventType getEventType() {
		return eventType;
	}

	public void setEventType(ThingEventType eventType) {
		this.eventType = eventType;
	}

}
