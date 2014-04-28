package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.UpcomingLotoConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingLotoWidgetConfiguration;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public class UpcomingScheduledLotoWidget extends ChartWidget<LocalDate, UpcomingLotoWidgetConfiguration> {

	@SpringBean
	private OrgPeriodSubtitleHelper orgPeriodSubtitleHelper;

	public UpcomingScheduledLotoWidget(String id, WidgetDefinition<UpcomingLotoWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<UpcomingLotoWidgetConfiguration>>(widgetDefinition));
		addPeriodButton("7days", 7);
		addPeriodButton("30days", 30);
		addPeriodButton("60days", 60);
		addPeriodButton("90days", 90);
        //setClickThruHandler(new ReportClickThruHandler(this,widgetDefinition.getId()));
	}

	@Override
	protected ChartData<LocalDate> getChartData() {
        DateChartManager chartManager = new DateChartManager(ChartGranularity.DAY, new DateRange(RangeType.forDays(period)));
        ChartSeries<LocalDate> results = dashboardReportingService.getUpcomingScheduledLoto(period);
		return new ChartData<LocalDate>(chartManager, results);
	}
		

	@Override
	protected FlotOptions<LocalDate> createOptions() {
		FlotOptions<LocalDate> options = super.createOptions();
		options.pan.interactive = false;
		options.xaxis.timeformat = "%b %d";
		return options;		
	}

	@Override
    public Component createConfigPanel(String id) {
		return new UpcomingLotoConfigPanel(id, getConfigModel());
	}
	
	@Override
	protected ChartGranularity getDefaultGranularity() {
		return null; // this widget doesn't use granularity...should not be relevant.
	}

	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgPeriodSubtitleHelper.getSubTitleModel(getWidgetDefinition(), null);
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );
	}
	
}
