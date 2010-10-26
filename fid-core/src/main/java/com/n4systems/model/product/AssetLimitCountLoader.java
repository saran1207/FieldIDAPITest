package com.n4systems.model.product;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetLimitCountLoader extends Loader<Long> implements LimitLoader {
	private Long tenantId;
	
	public AssetLimitCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Asset.class, filter);
		builder.addWhere(WhereClauseFactory.create("countsTowardsLimit", true));
		
		Long productCount = builder.getCount(em);
		return productCount;
	}

	public AssetLimitCountLoader setTenantId(Long tenantId) {
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
		return LimitType.ASSETS;
	}

}
