package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantByNameLoader extends Loader<Tenant> {
	private String tenantName;
	
	public TenantByNameLoader() {
		super();
	}

	@Override
	protected Tenant load(EntityManager em) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class);
		builder.addSimpleWhere("name", tenantName);
		
		Tenant tenant = builder.getSingleResult(em);
		return tenant;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = (tenantName != null) ? tenantName.toLowerCase() : null;
	}
	
}
