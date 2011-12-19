package com.n4systems.fieldid.wicket.pages.widgets;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.EventCompletenessConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;

@SuppressWarnings("serial")
public class EventCompletenessWidget extends ChartWidget<LocalDate,EventCompletenessWidgetConfiguration> {

	@SpringBean
	private DashboardReportingService reportingService;

	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

	public EventCompletenessWidget(String id, WidgetDefinition<EventCompletenessWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<EventCompletenessWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton("year", ChartGranularity.YEAR);
        addGranularityButton("quarter", ChartGranularity.QUARTER);
        addGranularityButton("month", ChartGranularity.MONTH);
        addGranularityButton("week", ChartGranularity.WEEK);        		
	}

	@Override
	protected ChartData<LocalDate> getChartData() {
		ChartData<LocalDate> chartData = new ChartData<LocalDate>(reportingService.getEventCompletenessEvents(granularity, getChartDateRange(), getOrg()));
		chartData.get(1).setColor("#60986B");
		return chartData;
	}
	
	private BaseOrg getOrg() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	private ChartDateRange getChartDateRange() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
	}
	
	@Override
	protected FlotOptions<LocalDate> createOptions() {
		FlotOptions<LocalDate> options = super.createOptions();
		options.pan.interactive = true;
		options.xaxis.timeformat = "%b %d";
		return options;		
	}

	@Override
    public Component createConfigPanel(String id) {
		return new EventCompletenessConfigPanel(id,getConfigModel());
	}
	
	@Override
	protected ChartGranularity getDefaultGranularity() {
		return ChartGranularity.WEEK;
	}

	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getChartDateRange());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );		
	}
	
}
