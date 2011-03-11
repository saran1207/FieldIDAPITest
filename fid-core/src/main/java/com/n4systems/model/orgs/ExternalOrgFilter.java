package com.n4systems.model.orgs;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ExternalOrgFilter implements QueryFilter {
	private final String pathPrefix;
	
	public ExternalOrgFilter(String prefix) {
		this.pathPrefix = prefix;
	}
	
	public ExternalOrgFilter() {
		this(null);
	}
	
	public void applyFilter(QueryBuilder<?> builder) {
		String customerOrgField = "customerOrg";
		String path = String.format("%s%s", (pathPrefix == null) ? "" : pathPrefix + ".", customerOrgField);
		String name = String.format("%s%s", (pathPrefix == null) ? "" : pathPrefix + "_", customerOrgField);
		
		builder.addWhere(Comparator.NOTNULL, name, path, "");
	}

}
