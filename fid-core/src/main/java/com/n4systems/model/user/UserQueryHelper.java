package com.n4systems.model.user;

import com.n4systems.util.persistence.QueryBuilder;

public class UserQueryHelper {

	public static QueryBuilder<?> applyFullyActiveFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("active", true).addSimpleWhere("deleted", false);
	}
}
