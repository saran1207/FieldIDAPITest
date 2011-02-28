package com.n4systems.model.user;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.util.persistence.QueryBuilder;

public class UserQueryHelper {

	public static QueryBuilder<?> applyFullyActiveFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("registered", true).addSimpleWhere("state", EntityState.ACTIVE);
	}
}
