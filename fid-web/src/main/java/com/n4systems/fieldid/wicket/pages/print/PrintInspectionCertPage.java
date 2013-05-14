package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.certificate.PrintAllCertificateService;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PrintInspectionCertPage extends PrintPage<EventReportCriteria> {

    @SpringBean
    private DownloadLinkService downloadLinkService;

    @SpringBean
    private PrintAllCertificateService printAllCertificateService;

    public PrintInspectionCertPage(IModel<EventReportCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        String reportName = String.format("%s Report - %s", EventReportType.INSPECTION_CERT.getDisplayName(), DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");
        DownloadLink link = downloadLinkService.createDownloadLink(reportName, ContentType.ZIP);
        return printAllCertificateService.generateEventCertificates(criteria.getObject(), EventReportType.INSPECTION_CERT, linkUrl, link);
    }

}
