package com.n4systems.fieldid.service.search.columns;

import com.n4systems.model.columns.ReportType;

public class ProcedureColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.PROCEDURE;
    }

    @Override
    protected boolean includeCustom() {
        return false;
    }
}
