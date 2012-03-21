package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.event.EventExcelExportService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ExportToExcelPage extends PrintPage<EventReportCriteria> {

    private @SpringBean EventExcelExportService excelExportService;

    public ExportToExcelPage(IModel<EventReportCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        final EventReportCriteria criteriaObject = criteria.getObject();
        storeLocalizedNamesInColumns(criteriaObject);

        String reportName = String.format("Event Report - %s", DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");

        return excelExportService.startTask(criteriaObject, reportName, linkUrl);
    }

    // Issue: The 'core' is responsible for generating the tables (excel etc). But the core has no concept of
    // localization. For now we will tell the back end what to use as the titles of its columns.
    private void storeLocalizedNamesInColumns(EventReportCriteria criteriaObject) {
        for (ColumnMappingView columnMappingView : criteriaObject.getSortedStaticAndDynamicColumns(true)) {
            String localizedTitle = new FIDLabelModel(columnMappingView.getLabel()).getObject();
            columnMappingView.setLocalizedLabel(localizedTitle);
        }
    }

}
