package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;

@SuppressWarnings("serial")
public class UpcomingScheduledEventsWidget extends ChartWidget<Calendar> {

	@SpringBean
	private DashboardReportingService reportingService;
	private Integer period = 30;

	public UpcomingScheduledEventsWidget(String id, WidgetDefinition widgetDefinition) {
		super(id, new Model<WidgetDefinition>(widgetDefinition));			
		addPeriodButton("7days", 7);
		addPeriodButton("30days", 30);
		addPeriodButton("60days", 60);
		addPeriodButton("90days", 90);
		add(new OrgForm("ownerForm"));
		setOutputMarkupId(true);
	}

	private void addPeriodButton(String id, final int period) {
        add(new IndicatingAjaxLink(id) {
			@Override public void onClick(AjaxRequestTarget target) {
				setPeriod(period);
				target.addComponent(UpcomingScheduledEventsWidget.this);
			}        	
        });      		
	}

	@Override
	protected List<ChartData<Calendar>> getChartData() {
		return reportingService.getUpcomingScheduledEvents(period, owner);
	}
	
	public void setPeriod(Integer period) {
		this.period = period;
	}
	
}
