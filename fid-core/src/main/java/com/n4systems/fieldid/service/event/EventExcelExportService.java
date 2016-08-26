package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.export.ExcelExportService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;

public class EventExcelExportService extends ExcelExportService<EventReportCriteria> {

    @Autowired
    private ReportService reportService;

    @Override
    protected int countTotalResults(EventReportCriteria criteria) {
        return reportService.countPages(criteria, 1L);
    }

    @Override
    protected PageHolder<TableView> performSearch(EventReportCriteria criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection) {
        return reportService.performSearch(criteria,resultTransformer,pageNumber, pageSize, useSelection);
    }
}
