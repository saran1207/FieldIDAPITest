package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunLastReportPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedReportService savedReportService;

    public RunLastReportPage() {
        final EventReportCriteria lastReport = savedReportService.retrieveLastSearch();
        if (lastReport != null) {
            setResponsePage(new ReportPage(lastReport).withSavedItemNamed("My Last Report"));
        } else {
            setResponsePage(DashboardPage.class);
        }
    }

}
