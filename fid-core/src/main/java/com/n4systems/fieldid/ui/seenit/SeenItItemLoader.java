package com.n4systems.fieldid.ui.seenit;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ui.seenit.SeenItStorageItem;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class SeenItItemLoader extends Loader<SeenItStorageItem>{

	protected Long userId;
	
	@Override
	public SeenItStorageItem load(EntityManager em) {
		guard();
		
		QueryBuilder<SeenItStorageItem> queryBuilder = new QueryBuilder<SeenItStorageItem>(SeenItStorageItem.class);
		queryBuilder.addSimpleWhere("userId", userId);
		
		return executeQuery(em, queryBuilder);
	}

	protected SeenItStorageItem executeQuery(EntityManager em, QueryBuilder<SeenItStorageItem> queryBuilder) {
		return queryBuilder.getSingleResult(em);
	}

	private void guard() {
		if (userId == null) {
			throw new InvalidArgumentException("User id must be set to load this.");
		}
	}

	public SeenItItemLoader setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

}
