package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.UpcomingProcedureAuditsConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.UpcomingProcedureAuditsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

@SuppressWarnings("serial")
public class UpcomingProcedureAuditsWidget extends ChartWidget<LocalDate,UpcomingProcedureAuditsWidgetConfiguration> {

    @SpringBean
    private OrgPeriodSubtitleHelper orgPeriodSubtitleHelper;

    public UpcomingProcedureAuditsWidget(String id, WidgetDefinition<UpcomingProcedureAuditsWidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<UpcomingProcedureAuditsWidgetConfiguration>>(widgetDefinition));
        addPeriodButton("7days", 7);
        addPeriodButton("30days", 30);
        addPeriodButton("60days", 60);
        addPeriodButton("90days", 90);
        setClickThruHandler(new ProcedureAuditsClickThruHandler(this,widgetDefinition.getId()));
    }

    @Override
    protected ChartData<LocalDate> getChartData() {
        DateChartManager chartManager = new DateChartManager(ChartGranularity.DAY, new DateRange(RangeType.forDays(period)));
        ChartSeries<LocalDate> results = dashboardReportingService.getUpcomingScheduledProcedureAudits(period, getOrg());
        return new ChartData<LocalDate>(chartManager, results);
    }

    private BaseOrg getOrg() {
        UpcomingProcedureAuditsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getOrg();
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
        return new UpcomingProcedureAuditsConfigPanel(id,getConfigModel());
    }

    @Override
    protected ChartGranularity getDefaultGranularity() {
        return null; // this widget doesn't use granularity...should not be relevant.
    }

    @Override
    protected IModel<String> getSubTitleModel() {
        SubTitleModelInfo info = orgPeriodSubtitleHelper.getSubTitleModel(getWidgetDefinition(), getOrg());
        return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );
    }
}
