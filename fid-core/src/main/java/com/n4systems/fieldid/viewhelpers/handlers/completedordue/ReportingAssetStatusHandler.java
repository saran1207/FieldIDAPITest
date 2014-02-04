package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;

public class ReportingAssetStatusHandler extends WebOutputHandler {

    public ReportingAssetStatusHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return findAssetStatus(value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return findAssetStatus(value);
    }

    private String findAssetStatus(Object value) {
        ThingEvent event = (ThingEvent) value;

        if (event.getWorkflowState() == WorkflowState.COMPLETED) {
            if (event.getAssetStatus() != null) {
                return event.getAssetStatus().getName();
            }

            return "";
        }

        if (event.getAsset().getAssetStatus() != null) {
            return event.getAsset().getAssetStatus().getName();
        }

        return "";
    }

}
