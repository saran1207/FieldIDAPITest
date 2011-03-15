package com.n4systems.fieldid.reporting.service;

import com.n4systems.model.columns.ReportType;

public class ScheduleColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.SCHEDULE;
    }

}
