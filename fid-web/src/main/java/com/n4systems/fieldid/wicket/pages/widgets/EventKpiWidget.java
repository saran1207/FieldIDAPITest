package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.pages.widgets.config.EventKPIConfigPanel;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.fieldid.wicket.util.AjaxCallback;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.reporting.EventKpiRecord;

public class EventKpiWidget extends Widget {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
	@SpringBean
	public OrgService orgService;

	public EventKpiWidget(String id, WidgetDefinition widgetDefinition) {
        super(id, new Model<WidgetDefinition>(widgetDefinition));
		setOutputMarkupId(true);
		
		add(new ListView<EventKpiRecord>("customerEventKpiList", getCustomerEventKPIs(getOrgList())) {

			@Override
			protected void populateItem(ListItem<EventKpiRecord> item) {
				EventKpiRecord eventKpiRecord = item.getModelObject();
				
				item.add(new Label("customerName", eventKpiRecord.getCustomer().getDisplayName()));
				item.add(new Label("scheduledEvents", eventKpiRecord.getTotalScheduledEvents() + ""));

				Long completedPercentage = getCompletedPercentage(eventKpiRecord);
				ProgressBar completedSchedules;

				item.add(completedSchedules = new ProgressBar("progressBar"));
				completedSchedules.setValue(completedPercentage.intValue());
				
				item.add(new Label("completedPercentage", completedPercentage + "%"));
				item.add(new Label("failedSchedules", eventKpiRecord.getFailed() + ""));
			}

		});
	}

	private Long getCompletedPercentage(EventKpiRecord record) {
		if(record.getTotalScheduledEvents() > 0L)
			return Math.round((record.getCompleted().doubleValue() * 100)/record.getTotalScheduledEvents().doubleValue());
		else 
			return 0L;
	}

	
	private List<BaseOrg> getOrgList() {
		EventKPIWidgetConfiguration config = (EventKPIWidgetConfiguration) getWidgetDefinition().getObject().getConfig();

		if(config == null) {
			return new ArrayList<BaseOrg>();
		}else {
			return config.getOrgs();
		}
	}

	private List<EventKpiRecord> getCustomerEventKPIs(List<BaseOrg> orgs) {
		
		List<EventKpiRecord> eventKpis = new ArrayList<EventKpiRecord>();
		for(BaseOrg org: orgs) {
			eventKpis.add(reportingService.getEventKpi(org));
		}
		return eventKpis;
	}
	
    @Override
    public <T extends WidgetConfiguration> WidgetConfigPanel createConfigurationPanel(String id, IModel<T> config, AjaxCallback<Boolean> saveCallback) {
        return new EventKPIConfigPanel(id, (IModel<EventKPIWidgetConfiguration>)config, saveCallback);
    }

}
