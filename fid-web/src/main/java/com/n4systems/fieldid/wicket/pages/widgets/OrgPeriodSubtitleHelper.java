package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.orgs.BaseOrg;


public class OrgPeriodSubtitleHelper extends OrgSubtitleHelper {
	
	public OrgPeriodSubtitleHelper() {
		super("config.chartPeriod.fromDisplayString","config.chartPeriod.toDisplayString");		
	}

	public SubTitleModelInfo getSubTitleModel(Object model, BaseOrg org) {
		return super.getSubTitleModel(model, org, "dateRange.week");
	}
	
}
