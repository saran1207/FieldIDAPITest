package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.pages.widgets.config.EventCompletenessConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class EventCompletenessWidget extends ChartWidget<Calendar,EventCompletenessWidgetConfiguration> {

	@SpringBean
	private DashboardReportingService reportingService;

	public EventCompletenessWidget(String id, WidgetDefinition<EventCompletenessWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<EventCompletenessWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);        		
	}

	@Override
	protected List<ChartSeries<Calendar>> getChartSeries() {
		return reportingService.getEventCompletenessEvents(granularity, getChartDateRange(), getOrg());
	}
	
	private BaseOrg getOrg() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	private ChartDateRange getChartDateRange() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
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
		return new EventCompletenessConfigPanel(id,getConfigModel());
	}
	
	@Override
	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.WEEK;
	}
	
}
