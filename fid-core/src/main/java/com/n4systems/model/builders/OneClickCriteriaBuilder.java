package com.n4systems.model.builders;

import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.StateSet;

public class OneClickCriteriaBuilder extends CriteriaBuilder<OneClickCriteria> {
	private final boolean principal;
    private final StateSet stateSet;
	
	public OneClickCriteriaBuilder(String text, boolean retired, boolean principal, StateSet stateSet) {
        super(text, retired);
		this.principal = principal;
        this.stateSet = stateSet;
	}
	
	public static OneClickCriteriaBuilder aCriteria() {
		return new OneClickCriteriaBuilder("text", false, false, null);
	}

	public OneClickCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, stateSet));
	}

	public OneClickCriteriaBuilder withRetired(boolean retired) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, stateSet));
	}

	public OneClickCriteriaBuilder withPrincipal(boolean principal) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, stateSet));
	}

	public OneClickCriteriaBuilder withStateSet(StateSet stateSet) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, stateSet));
	}
	
	@Override
	public OneClickCriteria createObject() {
		OneClickCriteria criteria = assignAbstractFields(new OneClickCriteria());
		criteria.setPrincipal(principal);
        criteria.setStates(stateSet);
		return criteria;
	}

}
