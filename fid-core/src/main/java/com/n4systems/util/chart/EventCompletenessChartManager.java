package com.n4systems.util.chart;

import com.n4systems.model.Event;
import com.n4systems.model.search.WorkflowState;
import com.n4systems.model.utils.DateRange;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;

public class EventCompletenessChartManager extends DateChartManager {

    private double BAR_RATIO = 0.8;  // instead of bars butting up against each other (1.0), scale them down to arbitrary smaller size to give whitespace.

    public EventCompletenessChartManager(ChartGranularity granularity, DateRange dateRange) {
        super(granularity, dateRange);
    }

    @Override
    public void sortSeries(ChartData<LocalDate> chartSeries) {
        Collections.sort(chartSeries, new EventCompletenessComparator());
    }

    @Override
    public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index, int size, int maxChartSeries) {
        super.updateOptions(chartSeries, options, index, size, maxChartSeries);
        setColor(chartSeries);

        options.bars.barWidth = BAR_RATIO * getGranularity().getStandardDuration().getMillis();
        // note : because of the fat bar width, we need to pull back the xaxis otherwise it will crop left half of first bar.
        if (options.xaxis.min!=null) {
            options.xaxis.min -= (long)((BAR_RATIO/4) * getGranularity().getStandardDuration().getMillis());
        }
    }

    private void setColor(ChartSeries<LocalDate> chartSeries) {
        Object id = chartSeries.getId();
        if (Event.WorkflowState.COMPLETED.equals(id)) {
            chartSeries.setColor("#60986B");
        } else if (Event.WorkflowState.CLOSED.equals(id)) {
            chartSeries.setColor("#666");
        } else if (Event.WorkflowState.OPEN.equals(id)) {
            ; // default color.
        }
    }

    @Override
    protected String getTooltipFormat(ChartGranularity granularity) {
        return super.getTooltipFormat(granularity).replace("{y}","{y}  ({total})" );
    }

    class EventCompletenessComparator implements Comparator<ChartSeries<LocalDate>> {

        // COMPLETED, then ALL_STATES
        @Override
        public int compare(ChartSeries<LocalDate> a, ChartSeries<LocalDate> b) {
            return ordinal(a.getId())- ordinal(b.getId());
        }

        public int ordinal(Object id) {
            if (WorkflowState.ALL_STATES.equals(id)) {
                return Event.WorkflowState.values().length;
            } else if (id instanceof WorkflowState) {
                WorkflowState status = (WorkflowState)id;
                return status.ordinal();
            } else {
                return 0;
            }
        }
    }

}
