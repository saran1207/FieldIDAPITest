package com.n4systems.model.user;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeeUserCountLoader extends Loader<Long> {
	private Long tenantId;
	
	public EmployeeUserCountLoader() {}

	@Override
	protected Long load(EntityManager em) {
		SecurityFilter filter = new SecurityFilter(tenantId);
		
		QueryBuilder<Long> builder = new QueryBuilder<Long>(UserBean.class, filter.prepareFor(UserBean.class));
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addWhere(new WhereParameter<Long>(Comparator.NULL, "r_EndUser"));
		
		Long userCount = builder.getCount(em);
		return userCount;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
