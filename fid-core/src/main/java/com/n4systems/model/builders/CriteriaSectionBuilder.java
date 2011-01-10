package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;

public class CriteriaSectionBuilder extends EntityWithTenantBuilder<CriteriaSection> {

	private String title;
	private boolean retired;
	private List<Criteria> criteria;
	
	public CriteriaSectionBuilder(String title, boolean retired, List<Criteria> criteria) {
		this.title = title;
		this.retired = retired;
		this.criteria = criteria;
	}
	
	public static CriteriaSectionBuilder aCriteriaSection() {
		return new CriteriaSectionBuilder("section_title", false, Collections.<Criteria>emptyList());
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

	public CriteriaSectionBuilder withCriteria(Criteria... criteria) {
		return new CriteriaSectionBuilder(title, retired, Arrays.asList(criteria));
	}
	
	@Override
	public CriteriaSection createObject() {
		CriteriaSection section = assignAbstractFields(new CriteriaSection());
		section.setTitle(title);
		section.setRetired(retired);
		section.setCriteria(criteria);
		return section;
	}
}
