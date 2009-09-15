package com.n4systems.model.producttype;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.TenantFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductTypeLoader extends TenantFilteredLoader<ProductType> {

	private Long id;


	public ProductTypeLoader(Long tenantId) {
		super(tenantId);
	}

	public ProductTypeLoader(SecurityFilter filter) {
		super(filter);
	}

	public ProductTypeLoader(Tenant tenant) {
		super(tenant);
	}

	public ProductTypeLoader(TenantOnlySecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductType load(EntityManager em, TenantOnlySecurityFilter filter) {
		if (id == null) {
			throw new InvalidArgumentException("you must give an id");
		}
		
		QueryBuilder<ProductType> query = getQueryBuilder(filter);
		query.addSimpleWhere("id", id);
		query.addPostFetchPaths("inspectionTypes", "schedules");
		
		
		return query.getSingleResult(em);
	}

	private QueryBuilder<ProductType> getQueryBuilder(TenantOnlySecurityFilter filter) {
		return new QueryBuilder<ProductType>(ProductType.class, filter);
	}
	
	public ProductTypeLoader setId(Long id) {
		this.id = id;
		return this;
	}	

}
