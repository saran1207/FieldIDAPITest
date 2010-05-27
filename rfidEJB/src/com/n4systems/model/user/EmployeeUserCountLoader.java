package com.n4systems.model.user;

import javax.persistence.EntityManager;


import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeeUserCountLoader extends Loader<Long> implements LimitLoader {
	private Long tenantId;
	
	public EmployeeUserCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		
		QueryBuilder<Long> builder = new QueryBuilder<Long>(User.class, filter);
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addWhere(new WhereParameter<Long>(Comparator.NULL, "owner.customerOrg"));
		
		Long userCount = builder.getCount(em);
		return userCount;
	}

	public EmployeeUserCountLoader setTenantId(Long tenantId) {
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
		return LimitType.EMPLOYEE_USERS;
	}
}
