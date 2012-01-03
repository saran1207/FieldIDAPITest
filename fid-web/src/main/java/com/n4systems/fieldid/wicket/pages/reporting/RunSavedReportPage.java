package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSavedReportPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedReportService savedReportService;

    public RunSavedReportPage(PageParameters params) {
        Long id = params.get("id").toLong();

        SavedReport savedReport = savedReportService.getSavedReport(id);
        EventReportCriteriaModel criteriaModel = savedReportService.convertToCriteria(id);
        criteriaModel.setSavedReportId(id);
        criteriaModel.setSavedReportName(savedReport.getName());

        setResponsePage(new ReportingResultsPage(criteriaModel));
    }

}
