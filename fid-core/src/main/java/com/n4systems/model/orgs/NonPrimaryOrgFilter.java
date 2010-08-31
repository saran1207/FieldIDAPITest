package com.n4systems.model.orgs;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class NonPrimaryOrgFilter implements QueryFilter {

	public PrimaryOrg primaryOrg;
	
	
	public NonPrimaryOrgFilter(PrimaryOrg primaryOrg) {
		super();
		this.primaryOrg = primaryOrg;
	}


	public void applyFilter(QueryBuilder<?> builder) {
		builder.addWhere(Comparator.NE, "id", "id", primaryOrg.getId());
	}

}
