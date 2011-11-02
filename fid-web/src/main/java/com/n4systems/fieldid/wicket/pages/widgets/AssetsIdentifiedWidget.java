package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.ChartSeries;

@SuppressWarnings("serial")
public class AssetsIdentifiedWidget extends ChartWidget<Calendar> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public AssetsIdentifiedWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);
    }

	@Override
    protected List<ChartSeries<Calendar>> getChartSeries() {
    	return reportingService.getAssetsIdentified(getChartDateRange(), granularity, getOrg());
    }

	private ChartDateRange getChartDateRange() {
		AssetsIdentifiedWidgetConfiguration config = (AssetsIdentifiedWidgetConfiguration) getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
	}

	private BaseOrg getOrg() {
		AssetsIdentifiedWidgetConfiguration config = (AssetsIdentifiedWidgetConfiguration) getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}
	
}



