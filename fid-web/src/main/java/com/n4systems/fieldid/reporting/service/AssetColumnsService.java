package com.n4systems.fieldid.reporting.service;

import com.n4systems.model.columns.ReportType;

public class AssetColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.ASSET;
    }

}
