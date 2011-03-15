package com.n4systems.fieldid.reporting.service;

import com.n4systems.model.columns.ReportType;

public class EventColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.EVENT;
    }

}
