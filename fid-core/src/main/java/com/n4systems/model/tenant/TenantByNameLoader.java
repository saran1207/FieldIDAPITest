package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantByNameLoader extends Loader<Tenant> {
	private String tenantName;
	
	public TenantByNameLoader() {
		super();
	}

	@Override
	public Tenant load(EntityManager em) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class, new OpenSecurityFilter());
		builder.addSimpleWhere("name", tenantName);
		builder.addSimpleWhere("disabled", false);

		Tenant tenant = builder.getSingleResult(em);
		return tenant;
	}

	public TenantByNameLoader setTenantName(String tenantName) {
		this.tenantName = (tenantName != null) ? tenantName.toLowerCase() : null;
        return this;
	}
	
}
