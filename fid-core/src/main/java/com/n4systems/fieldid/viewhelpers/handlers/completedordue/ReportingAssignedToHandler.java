package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.user.User;

public class ReportingAssignedToHandler extends WebOutputHandler {

    public ReportingAssignedToHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return displayUserName((ThingEvent)value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return displayUserName((ThingEvent)value);
    }

    private String displayUserName(ThingEvent event) {
        if (event.getWorkflowState() == WorkflowState.COMPLETED  && event.getAssignedTo() != null) {
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
