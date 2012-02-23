package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionPanel;
import com.n4systems.fieldid.wicket.pages.print.ExportToExcelPage;
import com.n4systems.fieldid.wicket.pages.print.PrintInspectionCertPage;
import com.n4systems.fieldid.wicket.pages.print.PrintObservationCertReportPage;
import com.n4systems.fieldid.wicket.pages.print.PrintThisReportPage;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.EventStatus;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

public class ReportingMassActionPanel extends MassActionPanel {

    public ReportingMassActionPanel(String id, final IModel<EventReportCriteriaModel> reportCriteriaModel) {
        super(id);

        ReportingMassActionLink massUpdateLink;
        ReportingMassActionLink assignEventsToJobsLink;

        WebMarkupContainer printContainer = new WebMarkupContainer("printContainer");

        add(new MassActionLink<ExportToExcelPage>("exportToExcelLink", ExportToExcelPage.class, reportCriteriaModel));

        // TOOD: This link is being replaced in this iteration with the new report summary page.
//        add(new ReportingMassActionLink("summaryReportLink", "/summaryReport.action?searchId=%s", reportCriteriaModel));

        printContainer.add(new MassActionLink<PrintThisReportPage>("printThisReportLink", PrintThisReportPage.class, reportCriteriaModel));
        printContainer.add(new MassActionLink<PrintInspectionCertPage>("printSelectedPdfReportsLink", PrintInspectionCertPage.class, reportCriteriaModel));
        printContainer.add(new MassActionLink<PrintObservationCertReportPage>("printSelectedObservationReportsLink", PrintObservationCertReportPage.class, reportCriteriaModel));

        printContainer.setVisible(reportCriteriaModel.getObject().getEventStatus() == EventStatus.COMPLETE);

        add(printContainer);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        boolean searchIncludesSafetyNetwork = reportCriteriaModel.getObject().isIncludeSafetyNetwork();

        add(massUpdateLink = new ReportingMassActionLink("massUpdateLink", "/massUpdateEvents.action?searchId=%s", reportCriteriaModel));
        massUpdateLink.setVisible(sessionUser.hasAccess("editevent") && !searchIncludesSafetyNetwork);

        add(assignEventsToJobsLink = new ReportingMassActionLink("assignToJobsLink", "/selectJobToAssignEventsTo.action?searchId=%s&reportType=OBSERVATION_CERT", reportCriteriaModel));
        assignEventsToJobsLink.setVisible(FieldIDSession.get().getSecurityGuard().isProjectsEnabled() && sessionUser.hasAccess("createevent") && !searchIncludesSafetyNetwork);
    }

}
