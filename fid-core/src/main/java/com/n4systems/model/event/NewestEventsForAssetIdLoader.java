package com.n4systems.model.event;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

public class NewestEventsForAssetIdLoader extends ListLoader<ThingEvent> {

	private long assetId;
	
	public NewestEventsForAssetIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ThingEvent> load(EntityManager em, SecurityFilter filter) {
		
		Asset asset = em.find(Asset.class, assetId);

        Date date = ServiceLocator.getLastEventDateService().findLastEventDate(assetId);
		
		QueryBuilder<ThingEvent> query = new QueryBuilder<ThingEvent>(ThingEvent.class, filter);
		query.addSimpleWhere("completedDate", date);
		query.addSimpleWhere("asset", asset);
				
		return query.getResultList(em);
	}

	public NewestEventsForAssetIdLoader setAssetId(long assetId) {
		this.assetId = assetId;
		return this;
	}
}
