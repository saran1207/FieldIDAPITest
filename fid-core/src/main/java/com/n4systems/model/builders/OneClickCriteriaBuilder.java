package com.n4systems.model.builders;

import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Criteria;

public class OneClickCriteriaBuilder extends CriteriaBuilder<OneClickCriteria> {
	private final boolean principal;
	
	public OneClickCriteriaBuilder(String text, boolean retired, boolean principal) {
        super(text, retired);
		this.principal = principal;
	}
	
	public static OneClickCriteriaBuilder aCriteria() {
		return new OneClickCriteriaBuilder("text", false, false);
	}

	public OneClickCriteriaBuilder withText(String text) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal));
	}

	public OneClickCriteriaBuilder withRetired(boolean retired) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal));
	}

	public OneClickCriteriaBuilder withPrincipal(boolean principal) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal));
	}
	
	@Override
	public OneClickCriteria createObject() {
		OneClickCriteria criteria = assignAbstractFields(new OneClickCriteria());
		criteria.setPrincipal(principal);
		return criteria;
	}

}
