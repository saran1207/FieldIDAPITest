package com.n4systems.model.orgs;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.util.persistence.QueryBuilder;

public class OrgQueryHelper {

	public static QueryBuilder<?> applyActiveFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("state", EntityState.ACTIVE);
	}
	
	public static QueryBuilder<?> applyArchivedFilter(QueryBuilder<?> query) {
		return query.addSimpleWhere("state", EntityState.ARCHIVED);
	}
}
