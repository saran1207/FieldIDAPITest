package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.certificate.PrintAllCertificateService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PrintObservationCertReportPage extends PrintPage<EventReportCriteria> {

    @SpringBean
    private PrintAllCertificateService printAllCertificateService;

    public PrintObservationCertReportPage(IModel<EventReportCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        String reportName = String.format("%s Report - %s", EventReportType.OBSERVATION_CERT.getDisplayName(), DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");
        return printAllCertificateService.generateEventCertificates(criteria.getObject(), EventReportType.OBSERVATION_CERT, linkUrl, reportName);
    }

}
