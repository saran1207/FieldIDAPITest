package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.pages.widgets.config.ProceduresPublishedConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.ProceduresPublishedWidgetConfiguration;
import com.n4systems.model.utils.DateRange;
import com.n4systems.util.chart.*;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by rrana on 2014-04-22.
 */
public class ProceduresPublishedWidget extends ChartWidget<LocalDate, ProceduresPublishedWidgetConfiguration> implements HasDateRange {

    @SpringBean
    private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

    public ProceduresPublishedWidget(String id, WidgetDefinition<ProceduresPublishedWidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<ProceduresPublishedWidgetConfiguration>>(widgetDefinition));
        addGranularityButton(ChartGranularity.YEAR);
        addGranularityButton(ChartGranularity.QUARTER);
        addGranularityButton(ChartGranularity.MONTH);
        addGranularityButton(ChartGranularity.WEEK);
        addGranularityButton(ChartGranularity.DAY);
    }

    @Override
    protected FlotOptions<LocalDate> createOptions() {
        FlotOptions<LocalDate> options = super.createOptions();
        return options;
    }

    @Override
    protected ChartData<LocalDate> getChartData() {

        //TODO - LOOK AT THIS!!!

        ChartManager chartManager = new DateChartManager(granularity, getDateRange());
        List<ChartSeries<LocalDate>> results = dashboardReportingService.getProceduresPublished(getDateRange(), granularity);
        return new ChartData<LocalDate>(chartManager, results);
    }

    @Override
    public DateRange getDateRange() {
        ProceduresPublishedWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return getTimeZoneDateRange(config.getDateRangeType());
    }

    @Override
    public Component createConfigPanel(String id) {
        return new ProceduresPublishedConfigPanel(id,getConfigModel());
    }

    @Override
    protected ChartGranularity getDefaultGranularity() {
        return ChartGranularity.WEEK;
    }

    @Override
    protected IModel<String> getSubTitleModel() {
        OrgSubtitleHelper.SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), null, getDateRange().getRangeType());
        return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );

    }

    @Override
    protected boolean isGranularityAppicable(ChartGranularity buttonGranularity) {
        return isGranularityAppicable(buttonGranularity, getDateRange());
    }
}
