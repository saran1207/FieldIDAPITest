package com.n4systems.model.criteriasection;

import java.util.Arrays;
import java.util.List;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.builders.EntityWithTenantBuilder;
import com.n4systems.model.criteria.CriteriaBuilder;

public class CriteriaSectionBuilder extends EntityWithTenantBuilder<CriteriaSection> {
	private String title;
	private boolean retired;
	private List<Criteria> criteria;
	
	public CriteriaSectionBuilder(String title, boolean retired, List<Criteria> criteria) {
		super();
		this.title = title;
		this.retired = retired;
		this.criteria = criteria;
	}
	
	public static CriteriaSectionBuilder aCriteriaSection() {
		return new CriteriaSectionBuilder("section_title", false, Arrays.asList(CriteriaBuilder.aCriteria().build(), CriteriaBuilder.aCriteria().build()));
	}
	
	public CriteriaSectionBuilder withTitle(String title) {
		return new CriteriaSectionBuilder(title, retired, criteria);
	}
	
	public CriteriaSectionBuilder withRetired(boolean retired) {
		return new CriteriaSectionBuilder(title, retired, criteria);
	}
	
	public CriteriaSectionBuilder withCriteria(List<Criteria> criteria) {
		return new CriteriaSectionBuilder(title, retired, criteria);
	}
	
	@Override
	public CriteriaSection build() {
		CriteriaSection section = assignAbstractFields(new CriteriaSection());
		section.setTitle(title);
		section.setRetired(retired);
		section.setCriteria(criteria);
		return section;
	}
}
