package com.n4systems.util.chart;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventSchedule.ScheduleStatus;
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
    public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index) {
        super.updateOptions(chartSeries, options, index);
        if (chartSeries.getId().equals(EventSchedule.ScheduleStatus.COMPLETED)) {
            chartSeries.setColor("#60986B");    // completed = green.
        }
    }


    class EventCompletenessComparator implements Comparator<ChartSeries<LocalDate>> {

        // COMPLETED, then ALL_STATUS
        @Override
        public int compare(ChartSeries<LocalDate> a, ChartSeries<LocalDate> b) {
            return ordinal(a.getId())- ordinal(b.getId());
        }

        public int ordinal(Object id) {
            if (EventSchedule.ALL_STATUS.equals(id)) {
                return ScheduleStatus.values().length;
            } else if (id instanceof ScheduleStatus) {
                ScheduleStatus status = (ScheduleStatus)id;
                return status.ordinal();
            } else {
                return 0;
            }
        }
    }

}
