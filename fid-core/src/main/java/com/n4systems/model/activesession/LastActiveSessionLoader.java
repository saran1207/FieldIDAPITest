package com.n4systems.model.activesession;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class LastActiveSessionLoader extends Loader<ActiveSession> {
	
	Long tenantId;
	
	boolean excludeN4User = false;
	
	@Override
	public ActiveSession load(EntityManager em) {
		QueryBuilder<ActiveSession> builder = new QueryBuilder<ActiveSession>(ActiveSession.class);
		builder.addSimpleWhere("user.tenant.id", tenantId);
		builder.addOrder("lastTouched", false);		
		
		List<ActiveSession> results = builder.getResultList(em);
		
		if (results == null || results.isEmpty()) {
			return null;
		} else {
			if (excludeN4User) {
				for (ActiveSession session: results) {
					if (!session.getUser().isSystem()) {
						return session;
					}
				}
				return null;
			} else {
				return results.get(0);
			}
			
		}
	}
	
	public LastActiveSessionLoader setTenant(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	
	public LastActiveSessionLoader excludeN4User() {
		this.excludeN4User = true;
		return this;
	}

	

}
