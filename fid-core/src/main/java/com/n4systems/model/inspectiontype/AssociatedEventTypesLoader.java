package com.n4systems.model.inspectiontype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssociatedEventTypesLoader extends ListLoader<AssociatedEventType> {

	private AssetType assetType;
	private EventType eventType;
	
	public AssociatedEventTypesLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssociatedEventType> load(EntityManager em, SecurityFilter filter) {
		if (eventType == null && assetType == null) {
			throw new InvalidArgumentException("You must look up by asset type of inspection type.");
		}
 		
		QueryBuilder<AssociatedEventType> query = prepareQuery();
		
		return query.getResultList(em);
	}

	private QueryBuilder<AssociatedEventType> prepareQuery() {
		QueryBuilder<AssociatedEventType> query = new QueryBuilder<AssociatedEventType>(AssociatedEventType.class, new OpenSecurityFilter());
		if (assetType != null) {
			query.addSimpleWhere("assetType", assetType);
		}
		
		if (eventType != null) {
			query.addSimpleWhere("eventType", eventType);
		}
		
		query.addOrder("eventType.name");
		return query;
	}

	public AssociatedEventTypesLoader setAssetType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}

	public AssociatedEventTypesLoader setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	

}
