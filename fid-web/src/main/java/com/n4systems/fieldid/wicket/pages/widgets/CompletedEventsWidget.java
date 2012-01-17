package com.n4systems.fieldid.wicket.pages.widgets;

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
import com.n4systems.services.reporting.DashboardReportingService;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDateRange;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.chart.FlotOptions;

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
    	options.lines.fill = false;
    	options.colors = new String[]{"#32578B", "#5B8C62", "#B35045", "#999999" };
    	return options;
    }
    
	@Override
    protected ChartData<LocalDate> getChartData() {
    	return new ChartData<LocalDate>(reportingService.getCompletedEvents(getChartDateRange(), granularity, getOrg()));
    }

	private BaseOrg getOrg() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getOrg();
	}	

	@Override
	public ChartDateRange getChartDateRange() {
		CompletedEventsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
		return config.getDateRange();
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
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg(), getChartDateRange());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );
		
	}

	@Override
	protected boolean isGranularityAppicable(ChartGranularity buttonGranularity) {
		return isGranularityAppicable(buttonGranularity, getChartDateRange());
	}
	
}



