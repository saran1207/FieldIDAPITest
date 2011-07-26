package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSavedReportPage extends FieldIDLoggedInPage {

    @SpringBean
    private SavedReportService savedReportService;

    public RunSavedReportPage(PageParameters params) {
        Long id = params.getLong("id");

        SavedReport savedReport = savedReportService.getSavedReport(id);
        EventReportCriteriaModel criteriaModel = savedReportService.convertToCriteria(id);
        criteriaModel.setSavedReportId(id);
        criteriaModel.setSavedReportName(savedReport.getName());

        setRedirect(true);
        setResponsePage(new ReportingResultsPage(criteriaModel));
    }

}
