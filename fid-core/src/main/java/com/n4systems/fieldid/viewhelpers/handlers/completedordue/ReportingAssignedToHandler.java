package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;

public class ReportingAssignedToHandler extends WebOutputHandler {

    public ReportingAssignedToHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return displayUserName((Event)value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return displayUserName((Event)value);
    }

    private String displayUserName(Event event) {
        if (event.getEventState() == Event.EventState.COMPLETED  && event.getAssignedTo() != null) {
            return getDisplayUserName(event.getAssignedTo().getAssignedUser());
        }

        return getDisplayUserName(event.getAsset().getAssignedUser());
    }

    private String getDisplayUserName(User user) {
        if (user == null) {
            return "Unassigned";
        }
        return user.getDisplayName();
    }

}
