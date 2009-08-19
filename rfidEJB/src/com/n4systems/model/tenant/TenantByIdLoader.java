package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantByIdLoader extends Loader<Tenant> {
	private Long tenantId;
	
	public TenantByIdLoader() {
		super();
	}

	@Override
	protected Tenant load(EntityManager em) {
		QueryBuilder<Tenant> builder = new QueryBuilder<Tenant>(Tenant.class);
		builder.addSimpleWhere("id", tenantId);
		
		Tenant tenant = builder.getSingleResult(em);
		return tenant;
	}
	
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
