package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.chart.FlotOptions;
import com.n4systems.fieldid.wicket.components.chart.HorizBarChartFlotOptions;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public class AssetsStatusWidget extends ChartWidget<String> {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
    public AssetsStatusWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
        add(new OrgForm("ownerForm"));
        setOutputMarkupId(true);
    }

     @Override
    protected FlotOptions<String> createOptions() {
    	HorizBarChartFlotOptions<String> options = new HorizBarChartFlotOptions<String>();
    	options.grid.height = 350;
    	return options;
    }

	@Override
    protected List<ChartData<String>> getChartData() {
    	return reportingService.getAssetsStatus(owner);
    }


}



