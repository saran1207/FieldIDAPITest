package com.n4systems.model.user;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EmployeeUserCountLoader extends EntityLoader<Long> {
	private Long tenantId;
	
	public EmployeeUserCountLoader() {
		super();
	}
	
	public EmployeeUserCountLoader(PersistenceManager pm) {
		super(pm);
	}
	
	@Override
	protected Long load(PersistenceManager pm) {
		SecurityFilter filter = new SecurityFilter(tenantId);
		
		QueryBuilder<Long> builder = new QueryBuilder<Long>(UserBean.class, filter.prepareFor(UserBean.class));
		builder.setCountSelect();
		builder.addSimpleWhere("system", false);
		builder.addSimpleWhere("active", true);
		builder.addSimpleWhere("deleted", false);
		builder.addWhere(new WhereParameter<Long>(Comparator.NULL, "r_EndUser"));
		
		Long userCount = pm.find(builder);
		
		return userCount;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
