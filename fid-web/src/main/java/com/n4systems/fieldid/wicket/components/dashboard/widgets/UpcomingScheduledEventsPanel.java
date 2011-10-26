package com.n4systems.fieldid.wicket.components.dashboard.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.services.reporting.ReportingService;
import com.n4systems.util.chart.ChartData;

public class UpcomingScheduledEventsPanel extends ChartablePanel<Calendar> {

	@SpringBean
	private ReportingService reportingService;
	
	public UpcomingScheduledEventsPanel(String id) {
		super(id);
	}

	@Override
	protected List<ChartData<Calendar>> getChartData() {
		return reportingService.getUpcomingScheduledEvents();
	}

}
