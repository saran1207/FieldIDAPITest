package com.n4systems.model.orgs;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ExternalOrgFilter implements QueryFilter {

	public void applyFilter(QueryBuilder<?> builder) {
		builder.addWhere(Comparator.NOTNULL, "customerOrg", "customerOrg", "");
	}

}
