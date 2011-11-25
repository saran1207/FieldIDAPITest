package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;



public class OrgDateRangeSubtitleHelper extends OrgSubtitleHelper {

	private static final String keyFormat = "dateRange.%1$s";

	public OrgDateRangeSubtitleHelper() {
		super("config.dateRange.fromDateDisplayString", "config.dateRange.toDateDisplayString");
	}
	
	public SubTitleModelInfo getSubTitleModel(Object model, BaseOrg org, ChartDateRange dateRange) {
		return super.getSubTitleModel(model, org, getKeyForDateRange(dateRange));
	}
	
	private String getKeyForDateRange(ChartDateRange dateRange) {
		// e.g. transform LAST_WEEK, THIS_WEEK   --->   week
		String key = dateRange.toString().replace("LAST_", "").replace("THIS_", "").toLowerCase();		
		return String.format(keyFormat,key);
	}
		
	
}
