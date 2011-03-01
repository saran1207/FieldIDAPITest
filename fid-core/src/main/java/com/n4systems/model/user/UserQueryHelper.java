package com.n4systems.model.user;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.util.persistence.QueryBuilder;

public class UserQueryHelper {

	public static QueryBuilder<?> applyFullyActiveFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("registered", true).addSimpleWhere("state", EntityState.ACTIVE);
	}
	
	public static QueryBuilder<?> applyActiveFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("state", EntityState.ACTIVE);
	}
	
	public static QueryBuilder<?> applyArchivedFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("state", EntityState.ARCHIVED);
	}
	
	public static QueryBuilder<?> applyRegisteredFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("registered", true);
	}
}
