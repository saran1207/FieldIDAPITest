package com.n4systems.model.inspection;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class LastInspectionLoader extends SecurityFilteredLoader<Inspection> {
	private Long productId;	
	
	public LastInspectionLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Inspection load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class, filter);
		builder.addWhere(WhereClauseFactory.create("product", productId));
		builder.addWhere(WhereClauseFactory.createNoVariable("date", "product.lastInspectionDate"));
		
		Inspection lastInspection = builder.getSingleResult(em);
		return lastInspection;
	}

	public LastInspectionLoader setProductId(Long productId) {
		this.productId = productId;
		return this;
	}
}
