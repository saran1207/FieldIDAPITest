package com.n4systems.model.catalog;



import javax.persistence.EntityManager;

import com.n4systems.model.TenantOrganization;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogLoader extends Loader<Catalog> {

	private TenantOrganization tenant;
	
	public CatalogLoader() {
		super();
	}

	@Override
	protected Catalog load(EntityManager em) {
		QueryBuilder<Catalog> query = new QueryBuilder<Catalog>(Catalog.class).addSimpleWhere("tenant", tenant).addPostFetchPaths("publishedProductTypes", "publishedInspectionTypes");
		return query.getSingleResult(em);
	}

	public CatalogLoader setTenant(TenantOrganization tenant) {
		this.tenant = tenant;
		return this;
	}

}
