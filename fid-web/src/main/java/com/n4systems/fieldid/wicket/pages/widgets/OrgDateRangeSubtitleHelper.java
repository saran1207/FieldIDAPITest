package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.RangeType;

public class OrgDateRangeSubtitleHelper extends OrgSubtitleHelper {

	private static final String keyFormat = "dateRange.%1$s";

	public OrgDateRangeSubtitleHelper() {
		super("config.dateRange.fromDateDisplayString", "config.dateRange.toDateDisplayString");
	}
	
	public SubTitleModelInfo getSubTitleModel(Object model, BaseOrg org, RangeType dateRangeType) {
		return super.getSubTitleModel(model, org, getKeyForDateRange(dateRangeType));
	}
	
	private String getKeyForDateRange(RangeType dateRangeType) {
		// e.g. transform LAST_WEEK, THIS_WEEK   --->   week
		String key = dateRangeType.toString().replace("LAST_", "").replace("THIS_", "").toLowerCase();
		return String.format(keyFormat,key);
	}

}
