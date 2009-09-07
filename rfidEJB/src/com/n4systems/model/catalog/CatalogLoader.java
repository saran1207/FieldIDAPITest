package com.n4systems.model.catalog;



import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogLoader extends Loader<Catalog> {

	private Tenant tenant;
	
	public CatalogLoader() {
		super();
	}

	@Override
	protected Catalog load(EntityManager em) {
		QueryBuilder<Catalog> query = new QueryBuilder<Catalog>(Catalog.class, new OpenSecurityFilter()).addSimpleWhere("tenant", tenant).addPostFetchPaths("publishedProductTypes", "publishedInspectionTypes");
		return query.getSingleResult(em);
	}

	public CatalogLoader setTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
