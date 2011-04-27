package com.n4systems.model.activesession;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class LastActiveSessionLoader extends Loader<ActiveSession> {
	
	Long tenantId;
	
	@Override
	protected ActiveSession load(EntityManager em) {
		QueryBuilder<ActiveSession> builder = new QueryBuilder<ActiveSession>(ActiveSession.class);
		builder.addSimpleWhere("user.tenant.id", tenantId);
		builder.addOrder("lastTouched", false);		
		
		List<ActiveSession> results = builder.getResultList(em);
		
		return (results == null || results.isEmpty()) ? null : results.get(0);
	}
	
	public LastActiveSessionLoader setTenant(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

}
