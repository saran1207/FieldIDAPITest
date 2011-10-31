package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.chart.FlotOptions;
import com.n4systems.fieldid.wicket.components.chart.LineGraphOptions;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;

@SuppressWarnings("serial")
public class CompletedEventsWidget extends ChartWidget<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private ChartGranularity granularity = ChartGranularity.ALL;
	
    public CompletedEventsWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
		setOutputMarkupId(true);		
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);
        add(new OrgForm("ownerForm"));
    }

	@SuppressWarnings("rawtypes")
	private void addGranularityButton(String id, final ChartGranularity period) {
        add(new IndicatingAjaxLink(id) {
			@Override public void onClick(AjaxRequestTarget target) {
				setGranularity(period);
				target.addComponent(CompletedEventsWidget.this);
			}        	
        });         
	}
    
    private void setGranularity(ChartGranularity period) {
    	this.granularity = period;
    }        	

    @Override
    protected FlotOptions<Calendar> createOptions() {
    	FlotOptions<Calendar> options = new LineGraphOptions<Calendar>();
    	options.lines.fill = false;
    	options.colors = new String[]{"#32579DB", "#08973B", "#DED91B", "#F2302B" };
    	return options;
    }
    
	@Override
    protected List<ChartSeries<Calendar>> getChartSeries() {
    	return reportingService.getCompletedEvents(granularity, owner);
    }
	
}



