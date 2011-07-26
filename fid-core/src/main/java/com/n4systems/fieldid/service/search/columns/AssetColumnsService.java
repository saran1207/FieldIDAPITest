package com.n4systems.fieldid.service.search.columns;

import com.n4systems.model.columns.ReportType;

public class AssetColumnsService extends ColumnsService {

    @Override
    protected ReportType getReportType() {
        return ReportType.ASSET;
    }

}
