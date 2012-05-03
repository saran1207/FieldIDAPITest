package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.asset.AssetExcelExportService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ExportSearchToExcelPage extends PrintPage<AssetSearchCriteria> {

    private @SpringBean
    AssetExcelExportService excelExportService;

    public ExportSearchToExcelPage(IModel<AssetSearchCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        final AssetSearchCriteria criteriaObject = criteria.getObject();

        String reportName = String.format("Asset Report - %s", DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");

        return excelExportService.startTask(criteriaObject, reportName, linkUrl);
    }

}
