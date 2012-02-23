package com.n4systems.fieldid.service.event;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.certificate.CertificatePrinter;
import com.n4systems.fieldid.service.download.DownloadService;
import com.n4systems.fieldid.service.search.ReportService;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

public class EventPrintService extends DownloadService<EventReportCriteriaModel> {

    @Autowired private EventSummaryJasperGenerator eventSummaryJasperGenerator;
    @Autowired private ReportService reportService;

    public EventPrintService() {
        super("eventSummaryCert", ContentType.PDF);
    }

    @Override
    protected void generateFile(EventReportCriteriaModel criteria, File file, String linkName) throws ReportException {
        final List<Long> searchResults = reportService.idSearch(criteria);
        final List<Long> sortedIdList = sortSelectionBasedOnIndexIn(criteria.getSelection(), searchResults);

        new CertificatePrinter().printToPDF(eventSummaryJasperGenerator.generate(criteria, sortedIdList), file);
    }

}
