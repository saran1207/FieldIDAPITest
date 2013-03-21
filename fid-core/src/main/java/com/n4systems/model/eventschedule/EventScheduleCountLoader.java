package com.n4systems.model.eventschedule;


import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class EventScheduleCountLoader extends Loader<Long> {

	private Long tenantId;
	
	@Override
	public Long load(EntityManager em) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Event.class, filter);
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
		return builder.getCount(em);
	}
	
	public EventScheduleCountLoader setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	
}
