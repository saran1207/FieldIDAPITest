package com.n4systems.model.orgs;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class InternalOrgFilter implements QueryFilter {
	private final String pathPrefix;
	
	public InternalOrgFilter(String prefix) {
		this.pathPrefix = prefix;
	}
	
	public InternalOrgFilter() {
		this(null);
	}
	
	public void applyFilter(QueryBuilder<?> builder) {
		String prefix = (pathPrefix == null) ? "" : pathPrefix + "." ;
		builder.addWhere(Comparator.NULL, prefix + "customerOrg", "customerOrg", "");
	}

}
