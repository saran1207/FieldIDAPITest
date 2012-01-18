package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.utils.DateRange;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;

import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.reporting.EventKpiRecord;

public class EventKpiTable extends Panel {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private EventKPIWidgetConfiguration config;


	public EventKpiTable(String id, List<BaseOrg> orgList, EventKPIWidgetConfiguration eventKPIWidgetConfiguration) {
		super(id);
		this.config = eventKPIWidgetConfiguration;
		add(new ListView<EventKpiRecord>("customerEventKpiList", getCustomerEventKPIs(orgList)) {
			
			@Override
			protected void populateItem(ListItem<EventKpiRecord> item) {
				EventKpiRecord eventKpiRecord = item.getModelObject();
				
				item.add(new Label("customerName", eventKpiRecord.getCustomer().getDisplayName()));
				item.add(new Label("scheduledEvents", eventKpiRecord.getTotalScheduledEvents() + ""));

				Long completedPercentage = getCompletedPercentage(eventKpiRecord);
				ProgressBar completedSchedules;

				item.add(completedSchedules = new ProgressBar("progressBar"));
				completedSchedules.setValue(completedPercentage.intValue());

				item.add(new Label("completedSchedules", eventKpiRecord.getCompleted() + ""));
				item.add(new Label("completedPercentage", completedPercentage + "%"));
				item.add(new Label("failedSchedules", eventKpiRecord.getFailed() + ""));
			}

		});
	}
	
	private List<EventKpiRecord> getCustomerEventKPIs(List<BaseOrg> orgs) {
		
		List<EventKpiRecord> eventKpis = new ArrayList<EventKpiRecord>();
		for(BaseOrg org: orgs) {
			eventKpis.add(reportingService.getEventKpi(org, getDateRange()));
		}
		return eventKpis;
	}
	
	protected DateRange getDateRange() {
		return new DateRange(config.getRangeType());
	}

	private Long getCompletedPercentage(EventKpiRecord record) {
		if(record.getTotalScheduledEvents() > 0L)
			return Math.round((record.getCompleted().doubleValue() * 100)/record.getTotalScheduledEvents().doubleValue());
		else 
			return 0L;
	}

}
