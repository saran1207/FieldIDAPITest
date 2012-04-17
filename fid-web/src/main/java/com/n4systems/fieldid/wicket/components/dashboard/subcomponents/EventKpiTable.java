package com.n4systems.fieldid.wicket.components.dashboard.subcomponents;

import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.services.reporting.EventKpiRecord;
import com.n4systems.services.reporting.KpiType;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class EventKpiTable extends Panel {
	
	@SpringBean
	private DashboardReportingService reportingService;
	private EventKPIWidgetConfiguration config;
    private WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition;


    public EventKpiTable(String id, List<BaseOrg> orgList, EventKPIWidgetConfiguration eventKPIWidgetConfiguration, final WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition) {
		super(id);
		this.config = eventKPIWidgetConfiguration;
        this.widgetDefinition = widgetDefinition;

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

                BookmarkablePageLink completed = new BookmarkablePageLink("completed", RunReportPage.class, getParams(eventKpiRecord, KpiType.COMPLETED.getLabel()));
                completed.add(new Label("completedValue", eventKpiRecord.getCompleted()+""));
                item.add(completed);
				item.add(new Label("completedPercentage", completedPercentage + "%"));

                BookmarkablePageLink incomplete = new BookmarkablePageLink("incomplete", RunReportPage.class, getParams(eventKpiRecord, KpiType.INCOMPLETE.getLabel()));
                incomplete.add(new Label("incompleteValue", getIncomplete(eventKpiRecord)));
                item.add(incomplete);

                BookmarkablePageLink failedSchedules = new BookmarkablePageLink("failedSchedules", RunReportPage.class, getParams(eventKpiRecord, KpiType.FAILED.getLabel()));
                failedSchedules.add(new Label("failedValue", eventKpiRecord.getFailed()+""));
                item.add(failedSchedules);
            }

		});
	}

    private String getIncomplete(EventKpiRecord eventKpiRecord) {
        return eventKpiRecord.getInProgress() + eventKpiRecord.getScheduled() + "";
    }

    private PageParameters getParams(EventKpiRecord eventKpiRecord, String series) {
        PageParameters params = new PageParameters().add(AbstractSearchPage.SOURCE_PARAMETER, AbstractSearchPage.WIDGET_SOURCE).add(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER, widgetDefinition.getId());
        params.add(AbstractSearchPage.SERIES_PARAMETER, series);
        int orgIndex = widgetDefinition.getConfig().getOrgs().indexOf(eventKpiRecord.getCustomer());
        params.add(AbstractSearchPage.X_PARAMETER, orgIndex);
        return params;
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
