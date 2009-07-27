package com.n4systems.model.tenant;

import javax.persistence.EntityManager;

import com.n4systems.model.TenantOrganization;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantByIdLoader extends Loader<TenantOrganization> {
	private Long tenantId;
	
	public TenantByIdLoader() {
		super();
	}

	@Override
	protected TenantOrganization load(EntityManager em) {
		QueryBuilder<TenantOrganization> builder = new QueryBuilder<TenantOrganization>(TenantOrganization.class);
		builder.addSimpleWhere("id", tenantId);
		
		TenantOrganization tenant = builder.getSingleResult(em);
		return tenant;
	}
	
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
