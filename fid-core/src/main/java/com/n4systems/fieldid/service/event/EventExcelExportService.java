package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.export.ExcelExportService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.TableView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class EventExcelExportService extends ExcelExportService<EventReportCriteria> {

    static Logger logger = Logger.getLogger(EventExcelExportService.class);

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

    @Override
    protected void logTaskStartMsg(EventReportCriteria criteria, String downloadUrl, DownloadLink downloadLink) {
        super.logTaskStartMsg(criteria, downloadUrl, downloadLink);
        logger.info("Started Excel export of " + criteria.getSelection().getNumSelectedIds() + " events for download " + downloadLink);
    }
}
