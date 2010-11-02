package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssociatedEventTypeExistsLoader extends SecurityFilteredLoader<Boolean> {
	private EventType eventType;
	private AssetType assetType;
	
	
	public AssociatedEventTypeExistsLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Boolean> builder = new QueryBuilder<Boolean>(AssociatedEventType.class, filter);
		builder.addWhere(WhereClauseFactory.create("eventType", eventType));
		builder.addWhere(WhereClauseFactory.create("assetType", assetType));
		
		boolean exists = builder.entityExists(em);
		return exists;
	}

	public AssociatedEventTypeExistsLoader setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	public AssociatedEventTypeExistsLoader setAssetType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}

}
