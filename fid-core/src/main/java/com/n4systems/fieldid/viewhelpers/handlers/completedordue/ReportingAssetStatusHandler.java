package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.EventSchedule;

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
        EventSchedule eventSchedule = (EventSchedule) value;

        if (eventSchedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED && eventSchedule.getEvent() != null && eventSchedule.getEvent().getAssetStatus() != null) {
            return eventSchedule.getEvent().getAssetStatus().getName();
        }

        if (eventSchedule.getAsset().getAssetStatus() != null) {
            return eventSchedule.getAsset().getAssetStatus().getName();
        }

        return "";
    }

}
