package com.n4systems.services.reporting;

import com.n4systems.util.chart.StringChartable;

public class ActionsReportRecord extends StringChartable {

    private static final String tooltipFormat = "%s %s : %d";

    public ActionsReportRecord(String priority, Long count, String barType) {
        super(priority,count,count,String.format(tooltipFormat, barType, priority,count));
    }

}
