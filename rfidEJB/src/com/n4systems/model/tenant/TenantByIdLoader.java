package com.n4systems.model.tenant;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class TenantByIdLoader extends EntityLoader<TenantOrganization> {
	private Long tenantId;
	
	public TenantByIdLoader() {
		super();
	}

	public TenantByIdLoader(PersistenceManager pm) {
		super(pm);
	}

	@Override
	protected TenantOrganization load(PersistenceManager pm) {
		QueryBuilder<TenantOrganization> builder = new QueryBuilder<TenantOrganization>(TenantOrganization.class);
		builder.addSimpleWhere("id", tenantId);
		
		TenantOrganization tenant = pm.find(builder);
		return tenant;
	}
	
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
