package com.n4systems.model.activesession;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.Date;

public class ActiveSessionSaver extends Saver<ActiveSession>{

	@Override
	public void save(EntityManager em, ActiveSession newActiveSession) {
		removeExistingActiveSession(em, newActiveSession);
		saveSession(em, newActiveSession);
	}

	private void saveSession(EntityManager em, ActiveSession entity) {
		super.save(em, entity);
        if (!entity.getUser().isSystem()) {
            final Tenant tenant = entity.getUser().getTenant();
            tenant.setLastLoginUser(entity.getUser());
            tenant.setLastLoginTime(new Date());
            em.merge(tenant);
        }
	}

	private void removeExistingActiveSession(EntityManager em, ActiveSession newActiveSession) {
		ActiveSession currentSession = getCurrentActiveSession(em, newActiveSession);
		if (currentSession != null) {
			em.remove(currentSession);
			em.flush();
		}
	}

	private ActiveSession getCurrentActiveSession(EntityManager em, ActiveSession entity) {
		return getQueryBuilder().addSimpleWhere("userId", entity.getUser().getId()).getSingleResult(em);
	}

	protected QueryBuilder<ActiveSession> getQueryBuilder() {
		return new QueryBuilder<ActiveSession>(ActiveSession.class);
	}

}
