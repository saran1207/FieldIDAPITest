package com.n4systems.util.persistence.search.terms;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.WhereParameter;

public class OwnerTerm extends SimpleTerm<BaseOrg> {

	private static final long serialVersionUID = 1L;

	public OwnerTerm() {
		super();
	}

	public OwnerTerm(String path, BaseOrg value) {
		super(path, value);
	}
	
	protected WhereParameter<?> getWhereParameter() {
		BaseOrg owner = getValue();
		if (owner == null) {
			
		}
		
		
		//WhereParameter<Long> param = new WhereParameter<>(WhereParameter.Comparator.EQ, getField(), );
		
		
		
		return null;
	}


}
