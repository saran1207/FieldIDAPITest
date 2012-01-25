package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedReportItem;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSavedReportPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedReportService savedReportService;

    public RunSavedReportPage(PageParameters params) {
        Long id = params.get("id").toLong();

        SavedReportItem savedReportItem = savedReportService.getConvertedReport(SavedReportItem.class, id);

        savedReportItem.getSearchCriteria().setSavedReportId(id);
        savedReportItem.getSearchCriteria().setSavedReportName(savedReportItem.getName());

        setResponsePage(new ReportingResultsPage(savedReportItem));
    }

}
