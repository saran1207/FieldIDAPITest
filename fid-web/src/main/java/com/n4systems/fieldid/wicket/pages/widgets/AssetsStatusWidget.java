package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.HorizBarChartOptions;

@SuppressWarnings("serial")
public class AssetsStatusWidget extends ChartWidget<String,WidgetConfiguration> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public AssetsStatusWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));			
        add(new OrgForm("ownerForm"));
    }

     @Override
    protected FlotOptions<String> createOptions() {
    	HorizBarChartOptions<String> options = new HorizBarChartOptions<String>();
    	return options;
    }

	@Override
    protected List<ChartSeries<String>> getChartSeries() {
    	return reportingService.getAssetsStatus(owner);
    }

	@Override
	protected Component createConfigPanel(String id) {
		return new Label(id, "hello");
	}



}



