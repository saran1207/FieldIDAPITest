package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.pages.widgets.config.AssetsStatusConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsStatusWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartSeries;
import com.n4systems.util.chart.FlotOptions;
import com.n4systems.util.chart.HorizBarChartOptions;

@SuppressWarnings("serial")
public class AssetsStatusWidget extends ChartWidget<String,AssetsStatusWidgetConfiguration> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public AssetsStatusWidget(String id, WidgetDefinition<AssetsStatusWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<AssetsStatusWidgetConfiguration>>(widgetDefinition));			
    }

     @Override
    protected FlotOptions<String> createOptions() {
    	HorizBarChartOptions<String> options = new HorizBarChartOptions<String>();
    	return options;
    }

	@Override
    protected List<ChartSeries<String>> getChartSeries() {
    	return reportingService.getAssetsStatus(getChartDateRange(), getOrg());
    }

	private BaseOrg getOrg() {
		AssetsStatusWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}
		
	private ChartDateRange getChartDateRange() {
		AssetsStatusWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
	}
	
	@Override
	protected Component createConfigPanel(String id) {
		return new AssetsStatusConfigPanel(id,getConfigModel());
	}

}



