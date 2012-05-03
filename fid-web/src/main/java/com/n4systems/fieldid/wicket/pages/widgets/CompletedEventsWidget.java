package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.CompletedEventsConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.CompletedEventsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.DashboardReportingService;

import java.util.List;

@SuppressWarnings("serial")
public class CompletedEventsWidget extends ChartWidget<LocalDate, CompletedEventsWidgetConfiguration> implements HasDateRange {
	
	@SpringBean
	private DashboardReportingService reportingService;
	
	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;
	
    public CompletedEventsWidget(String id, WidgetDefinition<CompletedEventsWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<CompletedEventsWidgetConfiguration>>(widgetDefinition));			
        addGranularityButton(ChartGranularity.YEAR);
        addGranularityButton(ChartGranularity.QUARTER);
        addGranularityButton(ChartGranularity.MONTH);
        addGranularityButton(ChartGranularity.WEEK);
        addGranularityButton(ChartGranularity.DAY);
        setClickThruHandler(new ReportClickThruHandler(this, widgetDefinition.getId()));
    }
 
    @Override
    protected FlotOptions<LocalDate> createOptions() {
    	FlotOptions<LocalDate> options = super.createOptions();
    	return options;
    }
    
	@Override
    protected ChartData<LocalDate> getChartData() {
        ChartManager chartManager = new CompletedEventsChartManager(granularity, getDateRange());
        List<ChartSeries<LocalDate>> results = reportingService.getCompletedEvents(getDateRange(), granularity, getOrg());
		return new ChartData<LocalDate>(chartManager, results);
    }

	private BaseOrg getOrg() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	@Override
	public DateRange getDateRange() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return new DateRange(config.getDateRangeType());
	}
	
	@Override
    public Component createConfigPanel(String id) {
		return new CompletedEventsConfigPanel(id,getConfigModel());
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



