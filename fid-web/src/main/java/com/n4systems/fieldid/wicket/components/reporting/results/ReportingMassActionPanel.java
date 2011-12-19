package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionPanel;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

public class ReportingMassActionPanel extends MassActionPanel {

    public ReportingMassActionPanel(String id, final IModel<EventReportCriteriaModel> reportCriteriaModel) {
        super(id);

        ReportingMassActionLink massUpdateLink;
        ReportingMassActionLink assignEventsToJobsLink;

        add(new ReportingMassActionLink("exportToExcelLink", "/aHtml/reportResults.action?searchId=%s", reportCriteriaModel));
        add(new ReportingMassActionLink("summaryReportLink", "/summaryReport.action?searchId=%s", reportCriteriaModel));
        add(new ReportingMassActionLink("printThisReportLink", "/aHtml/printReport.action?searchId=%s", reportCriteriaModel));
        add(new ReportingMassActionLink("printSelectedPdfReportsLink", "/aHtml/reportPrintAllCerts.action?searchId=%s&reportType=INSPECTION_CERT", reportCriteriaModel));
        add(new ReportingMassActionLink("printSelectedObservationReportsLink", "/aHtml/reportPrintAllCerts.action?searchId=%s&reportType=OBSERVATION_CERT", reportCriteriaModel));

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        boolean searchIncludesSafetyNetwork = reportCriteriaModel.getObject().isIncludeSafetyNetwork();

        add(massUpdateLink = new ReportingMassActionLink("massUpdateLink", "/massUpdateEvents.action?searchId=%s", reportCriteriaModel));
        massUpdateLink.setVisible(sessionUser.hasAccess("editevent") && !searchIncludesSafetyNetwork);

        add(assignEventsToJobsLink = new ReportingMassActionLink("assignToJobsLink", "/selectJobToAssignEventsTo.action?searchId=%s&reportType=OBSERVATION_CERT", reportCriteriaModel));
        assignEventsToJobsLink.setVisible(FieldIDSession.get().getSecurityGuard().isProjectsEnabled() && sessionUser.hasAccess("createevent") && !searchIncludesSafetyNetwork);
    }

}
