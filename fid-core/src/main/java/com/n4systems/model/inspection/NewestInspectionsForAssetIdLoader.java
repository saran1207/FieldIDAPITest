package com.n4systems.model.inspection;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class NewestInspectionsForAssetIdLoader extends ListLoader<Event> {

	private long assetId;
	
	public NewestInspectionsForAssetIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Event> load(EntityManager em, SecurityFilter filter) {
		
		Asset asset = em.find(Asset.class, assetId);
		
		QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, filter);
		query.addSimpleWhere("date", asset.getLastInspectionDate());
		query.addSimpleWhere("asset", asset);
				
		return query.getResultList(em);
	}

	public NewestInspectionsForAssetIdLoader setAssetId(long assetId) {
		this.assetId = assetId;
		return this;
	}
}
