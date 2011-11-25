package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.model.IModel;

import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;


public class OrgPeriodSubtitleHelper extends OrgSubtitleHelper {
	
	public OrgPeriodSubtitleHelper() {
		super("config.chartPeriod.fromDisplayString","config.chartPeriod.toDisplayString");		
	}

	public <W extends WidgetConfiguration> IModel<String> getSubTitleModel(Widget<W> widget, BaseOrg org) {
		return super.getSubTitleModel(widget, org, "dateRange.week");
	}
	
	
}
