package com.n4systems.model.builders;

import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ButtonGroup;

public class OneClickCriteriaBuilder extends CriteriaBuilder<OneClickCriteria> {
	private final boolean principal;
    private final ButtonGroup buttonGroup;
	
	public OneClickCriteriaBuilder(String text, boolean retired, boolean principal, ButtonGroup buttonGroup) {
        super(text, retired);
		this.principal = principal;
        this.buttonGroup = buttonGroup;
	}
	
	public static OneClickCriteriaBuilder aCriteria() {
		return new OneClickCriteriaBuilder("text", false, false, null);
	}

	public OneClickCriteriaBuilder withDisplayText(String text) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, buttonGroup));
	}

	public OneClickCriteriaBuilder withRetired(boolean retired) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, buttonGroup));
	}

	public OneClickCriteriaBuilder withPrincipal(boolean principal) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, buttonGroup));
	}

	public OneClickCriteriaBuilder withButtonGroup(ButtonGroup buttonGroup) {
		return makeBuilder(new OneClickCriteriaBuilder(text, retired, principal, buttonGroup));
	}
	
	@Override
	public OneClickCriteria createObject() {
		OneClickCriteria criteria = assignAbstractFields(new OneClickCriteria());
		criteria.setPrincipal(principal);
        criteria.setButtonGroup(buttonGroup);
		return criteria;
	}

}
