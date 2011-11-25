package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.pages.widgets.config.UpcomingEventsConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsWidget extends ChartWidget<Calendar,UpcomingEventsWidgetConfiguration> {

	@SpringBean
	private DashboardReportingService reportingService;

	public UpcomingScheduledEventsWidget(String id, WidgetDefinition<UpcomingEventsWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<UpcomingEventsWidgetConfiguration>>(widgetDefinition));			
		addPeriodButton("7days", 7);
		addPeriodButton("30days", 30);
		addPeriodButton("60days", 60);
		addPeriodButton("90days", 90);
	}

	@Override
	protected ChartData<Calendar> getChartData() {
		return new ChartData<Calendar>(reportingService.getUpcomingScheduledEvents(period, getOrg()));
	}
		
	private BaseOrg getOrg() {
		UpcomingEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}
		
	@Override
	protected FlotOptions<Calendar> createOptions() {
		FlotOptions<Calendar> options = super.createOptions();
		options.pan.interactive = false;
		options.xaxis.timeformat = "%b %d";
		return options;		
	}

	@Override
	protected Component createConfigPanel(String id) {
		return new UpcomingEventsConfigPanel(id,getConfigModel());
	}
	
	@Override
	protected ChartGranularity getDefaultGranularity() {
		return null; // this widget doesn't use granularity...should not be relevant.
	}

	@Override
	protected IModel<String> getSubTitleModel() {
		return getPeriodOrgSubTitleModel(getOrg());
	}
	
}
