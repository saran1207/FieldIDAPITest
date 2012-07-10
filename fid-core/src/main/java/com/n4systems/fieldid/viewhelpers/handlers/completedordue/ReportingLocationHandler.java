package com.n4systems.fieldid.viewhelpers.handlers.completedordue;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.Event;
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
        Event event = (Event) value;

        if (event.getEventState() == Event.EventState.COMPLETED) {
            return event.getAdvancedLocation().getFullName();
        }

        return event.getAsset().getAdvancedLocation().getFullName();
    }

}
