package com.n4systems.model.inspection;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class NewestInspectionsForProductIdLoader extends ListLoader<Inspection> {

	private long productId;	
	
	public NewestInspectionsForProductIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Inspection> load(EntityManager em, SecurityFilter filter) {
		
		Asset asset = em.find(Asset.class, productId);
		
		QueryBuilder<Inspection> query = new QueryBuilder<Inspection>(Inspection.class, filter);
		query.addSimpleWhere("date", asset.getLastInspectionDate());
		query.addSimpleWhere("asset", asset);
				
		return query.getResultList(em);
	}

	public NewestInspectionsForProductIdLoader setProductId(long productId) {
		this.productId = productId;
		return this;
	}
}
