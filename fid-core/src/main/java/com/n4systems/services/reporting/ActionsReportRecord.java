package com.n4systems.services.reporting;

import com.n4systems.util.chart.StringChartable;

public class ActionsReportRecord extends StringChartable {


    public ActionsReportRecord(String priority, Long count) {
        super(priority,count,count,priority);
    }

}
