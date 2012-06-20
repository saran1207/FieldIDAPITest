package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.EventSchedule;

public class ReportingLocationHandler extends WebOutputHandler {

    public ReportingLocationHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return findLocation(value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return findLocation(value);
    }

    private String findLocation(Object value) {
        EventSchedule schedule = (EventSchedule) value;

        if (schedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED && schedule.getEvent() != null) {
            return schedule.getEvent().getAdvancedLocation().getFullName();
        }

        return schedule.getAsset().getAdvancedLocation().getFullName();
    }

}
