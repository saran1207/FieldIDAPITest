package com.n4systems.util.chart;

import com.n4systems.model.Event;
import com.n4systems.model.search.WorkflowState;
import com.n4systems.model.utils.DateRange;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;

public class EventCompletenessChartManager extends DateChartManager {

    public EventCompletenessChartManager(ChartGranularity granularity, DateRange dateRange) {
        super(granularity,dateRange);
    }

    @Override
    public void sortSeries(ChartData<LocalDate> chartSeries) {
        Collections.sort(chartSeries, new EventCompletenessComparator());
    }

    @Override
    public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index, int size, int maxChartSeries) {
        super.updateOptions(chartSeries, options, index, size, maxChartSeries);
        if (chartSeries.getId().equals(WorkflowState.COMPLETE)) {
            chartSeries.setColor("#60986B");    // completed = green.
        }
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
