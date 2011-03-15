package com.n4systems.fieldid.reporting.service;

import com.n4systems.model.columns.ReportType;

public class EventToJobColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.EVENT_TO_JOB;
    }

    @Override
    protected boolean includeCustom() {
        return false;
    }
}
