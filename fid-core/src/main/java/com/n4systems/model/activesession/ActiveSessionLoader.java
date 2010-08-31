package com.n4systems.model.activesession;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class ActiveSessionLoader extends Loader<ActiveSession> implements IdLoader<Loader<ActiveSession>> {
	
	private Long userIdentifier;

	@Override
	protected ActiveSession load(EntityManager em) {
		gaurd();
		return getQueryBuilder().addSimpleWhere("user.id", userIdentifier).getSingleResult(em);
	}

	private void gaurd() {
		if (userIdentifier == null) {
			throw new InvalidArgumentException("you must provide a user id to look up with");
		}
	}

	protected QueryBuilder<ActiveSession> getQueryBuilder() {
		return new QueryBuilder<ActiveSession>(ActiveSession.class);
	}

	public ActiveSessionLoader setId(Long userIdentifier) {
		this.userIdentifier = userIdentifier;
		return this;
	}

}
