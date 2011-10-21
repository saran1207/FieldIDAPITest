package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.services.reporting.ReportingService;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public class AssetsIdentifiedPanel extends ChartablePanel<Calendar> {
	
	@SpringBean
	private ReportingService reportingService;
	
    public AssetsIdentifiedPanel(String id) {
        super(id);
    }

    @Override
    protected ChartData<Calendar> getChartData() {
    	return reportingService.getAssetsIdentified(new Date(), null);
    }
    

}



