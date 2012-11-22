package com.n4systems.fieldid.wicket.pages.print;

import com.n4systems.fieldid.service.certificate.PrintAllCertificateService;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.DateHelper;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PrintAllCertificatesPage extends PrintPage<AssetSearchCriteria> {

    @SpringBean PrintAllCertificateService printAllCertificateService;

    public PrintAllCertificatesPage(IModel<AssetSearchCriteria> criteria) {
        super(criteria);
    }

    @Override
    protected DownloadLink createDownloadLink() {
        String reportName = String.format("Manufacturer Certificate Report - %s", DateHelper.getFormattedCurrentDate(getCurrentUser()));
        String linkUrl = ContextAbsolutizer.toAbsoluteUrl("showDownloads.action?fileId=");

        return printAllCertificateService.generateAssetCertificates(criteria.getObject(), linkUrl, reportName);
    }

}
