package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.pages.widgets.config.CompletedEventsConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class CompletedEventsWidget extends ChartWidget<Calendar, CompletedEventsWidgetConfiguration> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public CompletedEventsWidget(String id, WidgetDefinition<CompletedEventsWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<CompletedEventsWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);
    }
 
    @Override
    protected FlotOptions<Calendar> createOptions() {
    	FlotOptions<Calendar> options = super.createOptions();
    	options.lines.fill = false;
    	options.colors = new String[]{"#32578B", "#5B8C62", "#B35045", "#999999" };
        options.tooltipFormat = getTooltipFormat(granularity);
    	return options;
    }
    
	@Override
    protected ChartData<Calendar> getChartData() {
    	return new ChartData<Calendar>(reportingService.getCompletedEvents(getChartDateRange(), granularity, getOrg()));
    }

	private BaseOrg getOrg() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	private ChartDateRange getChartDateRange() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
	}
	
	@Override
	protected Component createConfigPanel(String id) {
		return new CompletedEventsConfigPanel(id,getConfigModel());
	}
	
	@Override
	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.WEEK;   // range = QUARTER. 
	}
}



