package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.user.User;

public class ReportingAssignedToHandler extends WebOutputHandler {

    public ReportingAssignedToHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return displayUserName((EventSchedule)value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return displayUserName((EventSchedule)value);
    }

    private String displayUserName(EventSchedule schedule) {
        if (schedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED && schedule.getEvent() != null && schedule.getEvent().getAssignedTo() != null) {
            return getDisplayUserName(schedule.getEvent().getAssignedTo().getAssignedUser());
        }

        return getDisplayUserName(schedule.getAsset().getAssignedUser());
    }

    private String getDisplayUserName(User user) {
        if (user == null) {
            return "Unassigned";
        }
        return user.getDisplayName();
    }

}
