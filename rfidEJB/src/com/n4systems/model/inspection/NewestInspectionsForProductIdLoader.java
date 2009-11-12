package com.n4systems.model.inspection;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
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
		
		Product product = em.find(Product.class, productId);
		
		QueryBuilder<Inspection> query = new QueryBuilder<Inspection>(Inspection.class, filter);
		query.addSimpleWhere("date", product.getLastInspectionDate());
		query.addSimpleWhere("product", product);
				
		return query.getResultList(em);
	}

	public NewestInspectionsForProductIdLoader setProductId(long productId) {
		this.productId = productId;
		return this;
	}
}
