package com.n4systems.fieldid.service.search.columns;

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
