package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.persistence.QueryBuilder;

public class SecondaryOrgCountLoader extends Loader<Long> implements LimitLoader {
	private Long tenantId;
	
	public SecondaryOrgCountLoader() {}
	
	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(SecondaryOrg.class, filter);
		
		Long orgCount = builder.getCount(em);
		return orgCount;
	}

	public SecondaryOrgCountLoader setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	
	public void setTenant(Tenant tenant) {
		setTenantId(tenant.getId());
	}
	
	public long getLimit(Transaction transaction) {
		return load(transaction);
	}

	public LimitType getType() {
		return LimitType.SECONDARY_ORGS;
	}
}
