package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;



public class OrgDateRangeSubtitleHelper extends OrgSubtitleHelper {

	private static final String keyFormat = "dateRange.%1$s";

	public OrgDateRangeSubtitleHelper() {
		super("config.dateRange.fromDateDisplayString", "config.dateRange.toDateDisplayString");
	}
	
	public <W extends WidgetConfiguration> IModel<String> getSubTitleModel(Widget<W> widget, BaseOrg org, ChartDateRange dateRange) {
		return super.getSubTitleModel(widget, org, getKeyForDateRange(dateRange));
	}
	
	private String getKeyForDateRange(ChartDateRange dateRange) {
		// e.g. transform LAST_WEEK, THIS_WEEK   --->   week
		String key = dateRange.toString().replace("LAST_", "").replace("THIS_", "").toLowerCase();		
		return String.format(keyFormat,key);
	}
	
	
	
}
