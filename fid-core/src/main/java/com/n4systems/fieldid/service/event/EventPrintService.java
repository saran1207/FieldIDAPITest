package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.certificate.CertificatePrinter;
import com.n4systems.fieldid.service.download.DownloadService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.util.List;

public class EventPrintService extends DownloadService<EventReportCriteria> {

    static Logger logger = Logger.getLogger(EventPrintService.class);

    @Autowired private EventSummaryJasperGenerator eventSummaryJasperGenerator;
    @Autowired private ReportService reportService;

    public EventPrintService() {
        super("eventSummaryCert", ContentType.PDF);
    }

    @Override
    public void generateFile(EventReportCriteria criteria, OutputStream oStream, boolean useSelection, int resultLimit, int pageSize) throws ReportException {
        final List<Long> searchResults = reportService.idSearch(criteria);
        final List<Long> sortedIdList = sortSelectionBasedOnIndexIn(criteria.getSelection(), searchResults);

        new CertificatePrinter().printToPDF(eventSummaryJasperGenerator.generate(criteria, sortedIdList), oStream);
    }

    @Override
    protected void logTaskStartMsg(EventReportCriteria criteria, String downloadUrl, DownloadLink downloadLink) {
        super.logTaskStartMsg(criteria, downloadUrl, downloadLink);
        logger.info("Started Excel print of " + criteria.getSelection().getNumSelectedIds() + " events for download " + downloadLink);
    }
}
