package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class MassActionPanel extends Panel {

    private StringBuffer variableAssignmentScriptBuffer;

    public MassActionPanel(String id) {
        super(id);

        variableAssignmentScriptBuffer = new StringBuffer();

        addSizeParameterLabel("maxSizeForMassUpdate", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId()));
        addSizeParameterLabel("maxSizeForExcelExport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId()));
        addSizeParameterLabel("maxSizeForPDFPrintOuts", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId()));
        addSizeParameterLabel("maxSizeForSummaryReport", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_SUMMARY_REPORT, getTenantId()));
        addSizeParameterLabel("maxSizeForMultiEvent", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId()));
        addSizeParameterLabel("maxSizeForAssigningEventsToJobs", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MAX_SIZE_FOR_ASSIGNING_INSPECTIONS_TO_JOBS, getTenantId()));
        addSizeParameterLabel("maxSizeForMassSchedule", ConfigContext.getCurrentContext().getInteger(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId()));

        variableAssignmentScriptBuffer.append("addLightboxListeners();");
        variableAssignmentScriptBuffer.append("addLimitGuards();");
    }

    protected void addSizeParameterLabel(String labelKey, Integer size) {
        add(new Label(labelKey, size.toString()));
        variableAssignmentScriptBuffer.append(labelKey).append(" = ").append(size).append(";\n");
    }

    protected Long getTenantId() {
        return FieldIDSession.get().getSessionUser().getTenant().getId();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavaScript(variableAssignmentScriptBuffer.toString());
    }
}
