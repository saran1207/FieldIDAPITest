package com.n4systems.model.user;



import java.util.Arrays;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.security.UserType;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeeUserCountLoader extends Loader<Long> implements LimitLoader {
	private Long tenantId;

	public EmployeeUserCountLoader() {
	}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		QueryBuilder<Long> builder = new QueryBuilder<Long>(User.class, filter);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "userType", Arrays.asList(UserType.ADMIN, UserType.FULL)));
		UserQueryHelper.applyFullyActiveFilter(builder);

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
