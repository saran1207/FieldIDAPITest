package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.event.EventPrintService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PrintThisReportPage extends PrintPage<EventReportCriteria> {

    private @SpringBean EventPrintService eventPrintService;

    public PrintThisReportPage(IModel<EventReportCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        String reportName = String.format("Event Summary Report - %s", DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");
        return eventPrintService.startTask(criteria.getObject(), reportName, linkUrl);
    }

}
