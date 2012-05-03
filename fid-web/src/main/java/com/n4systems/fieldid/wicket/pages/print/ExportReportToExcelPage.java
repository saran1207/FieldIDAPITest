package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.event.EventExcelExportService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ExportReportToExcelPage extends PrintPage<EventReportCriteria> {

    private @SpringBean EventExcelExportService excelExportService;

    public ExportReportToExcelPage(IModel<EventReportCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        final EventReportCriteria criteriaObject = criteria.getObject();

        String reportName = String.format("Event Report - %s", DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");

        return excelExportService.startTask(criteriaObject, reportName, linkUrl);
    }

}
