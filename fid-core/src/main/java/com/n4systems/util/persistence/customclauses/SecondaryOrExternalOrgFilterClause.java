package com.n4systems.util.persistence.customclauses;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.WhereParameter;

public class SecondaryOrExternalOrgFilterClause extends WhereParameter<Long> {
	private static final long serialVersionUID = 1L;
	
	public SecondaryOrExternalOrgFilterClause(String name, String orgPath, BaseOrg org, ChainOp chainOp) {
		super(Comparator.EQ, name, formatFilterPath(orgPath, org), org.getId(), null, false, chainOp);
	}
	
	private static String formatFilterPath(String orgPath, BaseOrg org) {
		if (org.isPrimary()) {
			throw new InvalidArgumentException("PrimaryOrg passed to SecondaryOrExternalOrgFilterClause");
		}
		
		return String.format("%s.%s", orgPath, org.getFilterPath());
	}
}
