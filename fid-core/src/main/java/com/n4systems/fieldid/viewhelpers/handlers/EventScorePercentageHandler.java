package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.model.Event;

import java.text.NumberFormat;

public class EventScorePercentageHandler extends WebOutputHandler {

    private NumberFormat percentFormat = NumberFormat.getPercentInstance();

    public EventScorePercentageHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
        percentFormat.setMaximumFractionDigits(2);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return getEventScorePercentage(value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return getEventScorePercentage(value);
    }
    
    private String getEventScorePercentage(Object value) {
        Event event = (Event) value;

        if(event.getEventState() == Event.EventState.COMPLETED) {
            if(event.getScore() != null)
                return percentFormat.format(new EventFormHelper().getEventFormScorePercentage(event));
            else
                return "";
        }else
            return "";
    }
}
