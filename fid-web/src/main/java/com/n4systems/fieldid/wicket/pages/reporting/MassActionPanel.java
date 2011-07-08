package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.pages.reporting.action.LegacyReportMassActionLink;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.web.helper.SessionUser;

public class MassActionPanel extends Panel implements IHeaderContributor {

    private StringBuffer variableAssignmentScriptBuffer;
    private IModel<EventReportCriteriaModel> reportCriteriaModel;

    public MassActionPanel(String id, final IModel<EventReportCriteriaModel> reportCriteriaModel) {
        super(id);
        this.reportCriteriaModel = reportCriteriaModel;

        variableAssignmentScriptBuffer = new StringBuffer();

        addSizeParameterLabel("maxSizeForMassUpdate", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MASS_UPDATE, getTenantId()));
        addSizeParameterLabel("maxSizeForExcelExport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_EXCEL_EXPORT, getTenantId()));
        addSizeParameterLabel("maxSizeForPDFPrintOuts", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_PDF_PRINT_OUTS, getTenantId()));
        addSizeParameterLabel("maxSizeForSummaryReport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, getTenantId()));
        addSizeParameterLabel("maxSizeForMultiEvent", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_MULTI_INSPECT, getTenantId()));
        addSizeParameterLabel("maxSizeForAssigningEventsToJobs", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, getTenantId()));

        variableAssignmentScriptBuffer.append("addLimitGuards();");

        LegacyReportMassActionLink massUpdateLink;
        LegacyReportMassActionLink assignEventsToJobsLink;

        add(new LegacyReportMassActionLink("exportToExcelLink", "/aHtml/reportResults.action?searchId=%s", reportCriteriaModel));
        add(new LegacyReportMassActionLink("summaryReportLink", "/summaryReport.action?searchId=%s", reportCriteriaModel));
        add(new LegacyReportMassActionLink("printThisReportLink", "/aHtml/printReport.action?searchId=%s", reportCriteriaModel));
        add(new LegacyReportMassActionLink("printSelectedPdfReportsLink", "/aHtml/reportPrintAllCerts.action?searchId=%s&reportType=INSPECTION_CERT", reportCriteriaModel));
        add(new LegacyReportMassActionLink("printSelectedObservationReportsLink", "/aHtml/printReport.action?searchId=%s&reportType=OBSERVATION_CERT", reportCriteriaModel));

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        boolean searchIncludesSafetyNetwork = reportCriteriaModel.getObject().isIncludeSafetyNetwork();

        add(massUpdateLink = new LegacyReportMassActionLink("massUpdateLink", "/massUpdateEvents.action?searchId=%s", reportCriteriaModel));
        massUpdateLink.setVisible(sessionUser.hasAccess("editevent") && !searchIncludesSafetyNetwork);

        add(assignEventsToJobsLink = new LegacyReportMassActionLink("assignToJobsLink", "/selectJobToAssignEventsTo.action?searchId=%s&reportType=OBSERVATION_CERT", reportCriteriaModel));
        assignEventsToJobsLink.setVisible(FieldIDSession.get().getSecurityGuard().isProjectsEnabled() && sessionUser.hasAccess("createevent") && !searchIncludesSafetyNetwork);
    }

    private void addSizeParameterLabel(String labelKey, Integer size) {
        add(new Label(labelKey, size.toString()));
        variableAssignmentScriptBuffer.append(labelKey).append(" = ").append(size).append(";\n");
    }

    private Long getTenantId() {
        return FieldIDSession.get().getSessionUser().getTenant().getId();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavascript(variableAssignmentScriptBuffer.toString());
    }

}
