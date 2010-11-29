package com.n4systems.model.criteria;

import com.n4systems.model.Criteria;
import com.n4systems.model.builders.EntityWithTenantBuilder;

public class CriteriaBuilder extends EntityWithTenantBuilder<Criteria> {
	private final String text;
	private final boolean retired;
	private final boolean principal;
	
	public CriteriaBuilder(String text, boolean retired, boolean principal) {
		this.text = text;
		this.retired = retired;
		this.principal = principal;
	}
	
	public static CriteriaBuilder aCriteria() {
		return new CriteriaBuilder("text", false, false);
	}
	
	public CriteriaBuilder withText(String text) {
		return makeBuilder(new CriteriaBuilder(text, retired, principal));
	}
	
	public CriteriaBuilder withRetired(boolean retired) {
		return makeBuilder(new CriteriaBuilder(text, retired, principal));
	}
	
	public CriteriaBuilder withPrincipal(boolean principal) {
		return makeBuilder(new CriteriaBuilder(text, retired, principal));
	}
	
	@Override
	public Criteria createObject() {
		Criteria criteria = assignAbstractFields(new Criteria());
		criteria.setRetired(retired);
		criteria.setPrincipal(principal);
		return criteria;
	}
}
