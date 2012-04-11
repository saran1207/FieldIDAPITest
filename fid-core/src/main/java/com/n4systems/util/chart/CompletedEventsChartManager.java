package com.n4systems.util.chart;

import com.n4systems.model.Status;
import com.n4systems.model.utils.DateRange;
import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;

public class CompletedEventsChartManager extends DateChartManager {

    public CompletedEventsChartManager(ChartGranularity granularity, DateRange dateRange) {
        super(granularity, dateRange);
    }

    @Override
    public void sortSeries(ChartData<LocalDate> chartSeries) {
        Collections.sort(chartSeries, new CompletedEventsComparator());    
    }

    @Override
    public void updateOptions(ChartSeries<LocalDate> chartSeries, FlotOptions<LocalDate> options, int index) {
        super.updateOptions(chartSeries, options, index);
    	options.lines.fill = false;
                                        // All,   PASS,       FAIL,       N/A
    	options.colors = new String[]{"#999999", "#B35045", "#5B8C62",  "#32578B"  };
    }


    class CompletedEventsComparator implements Comparator<ChartSeries<LocalDate>> {

        // sort in reverse order of Status, with ALL_STATUS coming at the end.
        //  0:NA, 1:FAIL, 2:PASS, 3:ALL_STATUS.
        // assumes the id of each chartSeries is a Status object.
        @Override
        public int compare(ChartSeries<LocalDate> a, ChartSeries<LocalDate> b) {
            return ordinal(a.getId()) - ordinal(b.getId());
        }
        
        public int ordinal(Object id) {
            if (Status.ALL.equals(id)) {
                return Status.values().length;
            } else if (id instanceof Status) { 
                Status status = (Status)id;
                return status.values().length-1-status.ordinal();
            } else {
                return 0;
            }
        }
    }
}
