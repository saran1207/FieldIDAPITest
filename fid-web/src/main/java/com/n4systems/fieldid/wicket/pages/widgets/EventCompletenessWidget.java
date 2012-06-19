package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
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

import java.util.List;

@SuppressWarnings("serial")
public class EventCompletenessWidget extends ChartWidget<LocalDate,EventCompletenessWidgetConfiguration> implements HasDateRange {

	@SpringBean
	private DashboardReportingService reportingService;

	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

	public EventCompletenessWidget(String id, WidgetDefinition<EventCompletenessWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<EventCompletenessWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton(ChartGranularity.YEAR);
        addGranularityButton(ChartGranularity.QUARTER);
        addGranularityButton(ChartGranularity.MONTH);
        addGranularityButton(ChartGranularity.WEEK);        	
        addGranularityButton(ChartGranularity.DAY);
        // NOTE : since what reporting & dashboard widget display for ALL series are fundamentally different at this time, we shouldn't allow
        //   clickThru.  (June 2012).
        setClickThruHandler(new ReportClickThruHandler(this,widgetDefinition.getId()));
	}

	@Override
	protected ChartData<LocalDate> getChartData() {
        ChartManager chartManager = new EventCompletenessChartManager(granularity, getDateRange());
        List<ChartSeries<LocalDate>> results = reportingService.getEventCompletenessEvents(granularity, getDateRange(), getOrg());
        return new ChartData<LocalDate>(chartManager, results);
	}
	
	private BaseOrg getOrg() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	@Override
	public DateRange getDateRange() {
		EventCompletenessWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return new DateRange(config.getRangeType());
	}
	
	@Override
	protected FlotOptions<LocalDate> createOptions() {
		FlotOptions<LocalDate> options = super.createOptions();
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
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getDateRange().getRangeType());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );		
	}
	
	@Override
	protected boolean isGranularityAppicable(ChartGranularity buttonGranularity) {
		return isGranularityAppicable(buttonGranularity, getDateRange());
	}
}
