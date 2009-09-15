package com.n4systems.model.user;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeeUserCountLoader extends Loader<Long> {
	private Long tenantId;
	
	public EmployeeUserCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		
		QueryBuilder<Long> builder = new QueryBuilder<Long>(UserBean.class, filter);
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addWhere(new WhereParameter<Long>(Comparator.NULL, "owner.customerOrg"));
		
		Long userCount = builder.getCount(em);
		return userCount;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
