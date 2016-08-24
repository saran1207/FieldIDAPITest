package com.n4systems.model.builders;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		return new CriteriaSectionBuilder("section_title", false, new ArrayList<Criteria>());
	}
	
	public CriteriaSectionBuilder withTitle(String title) {
		return makeBuilder(new CriteriaSectionBuilder(title, retired, criteria));
	}
		
	public CriteriaSectionBuilder withDisplayText(String title) {
		return withTitle(title);
	}
		
	public CriteriaSectionBuilder withRetired(boolean retired) {
		return makeBuilder(new CriteriaSectionBuilder(title, retired, criteria));
	}
	
	public CriteriaSectionBuilder withCriteria(List<Criteria> criteria) {
		return makeBuilder(new CriteriaSectionBuilder(title, retired, criteria));
	}

	public CriteriaSectionBuilder withCriteria(Criteria... criteria) {
        List<Criteria> newList = new ArrayList<Criteria>();
        newList.addAll(Arrays.asList(criteria));
		return makeBuilder(new CriteriaSectionBuilder(title, retired, newList));
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
