package com.n4systems.util.chart;

import com.n4systems.services.reporting.CriteriaTrendsResultCountByCriteriaRecord;

import java.util.List;

public class CriteriaTrendsSortedByCountChartMap extends ChartableMap<String> {

    public CriteriaTrendsSortedByCountChartMap(List<CriteriaTrendsResultCountByCriteriaRecord> criteriaTrendsByCriteria) {
        super(new CriteriaTrendsSortedByCountComparator(criteriaTrendsByCriteria));
    }

}
