package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.LineGraphOptions;

@SuppressWarnings("serial")
public class CompletedEventsWidget extends ChartWidget<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public CompletedEventsWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);
        add(new OrgForm("ownerForm"));
    }

 
    @Override
    protected FlotOptions<Calendar> createOptions() {
    	FlotOptions<Calendar> options = new LineGraphOptions<Calendar>();
    	options.lines.fill = false;
    	options.colors = new String[]{"#32578B", "#5B8C62", "#B35045", "#999999" };
    	return options;
    }
    
	@Override
    protected List<ChartSeries<Calendar>> getChartSeries() {
    	return reportingService.getCompletedEvents(granularity, owner);
    }
	
}



