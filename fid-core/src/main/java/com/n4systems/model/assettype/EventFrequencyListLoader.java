package com.n4systems.model.assettype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class EventFrequencyListLoader extends ListLoader<AssetTypeSchedule> {
	
	private Long assetTypeId;
	private Long eventTypeId;
	
	
	public EventFrequencyListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<AssetTypeSchedule> load(EntityManager em, SecurityFilter filter) {
		if (eventTypeId == null && assetTypeId == null) {
			throw new InvalidArgumentException("you must choose an asset type or an event type or both.  You didn't select either");
		}
		
		return createQuery(em, filter);
	}

	private List<AssetTypeSchedule> createQuery(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetTypeSchedule> builder = new QueryBuilder<AssetTypeSchedule>(AssetTypeSchedule.class, filter);
		
		if (eventTypeId != null) {
			builder.addSimpleWhere("eventType.id", eventTypeId);
		}
		
		if (assetTypeId != null) {
			builder.addSimpleWhere("assetType.id", assetTypeId);
		}
		
		return builder.getResultList(em);
	}

	public Long getAssetTypeId() {
		return assetTypeId;
	}

	public EventFrequencyListLoader setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
		return this;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public EventFrequencyListLoader setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
		return this;
	}
	
}
