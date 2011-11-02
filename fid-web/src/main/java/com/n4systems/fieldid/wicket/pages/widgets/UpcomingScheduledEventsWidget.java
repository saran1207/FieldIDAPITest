package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsWidget extends ChartWidget<Calendar,WidgetConfiguration> {

	@SpringBean
	private DashboardReportingService reportingService;

	public UpcomingScheduledEventsWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));			
		addPeriodButton("7days", 7);
		addPeriodButton("30days", 30);
		addPeriodButton("60days", 60);
		addPeriodButton("90days", 90);
		add(new OrgForm("ownerForm"));
	}

	@Override
	protected List<ChartSeries<Calendar>> getChartSeries() {
		return reportingService.getUpcomingScheduledEvents(period, owner);
	}
	

	@Override
	protected FlotOptions<Calendar> createOptions() {
		FlotOptions<Calendar> options = super.createOptions();
		options.pan.interactive = false;
		options.xaxis.timeformat = "%b %d";
		return options;
		
	}
	
}
