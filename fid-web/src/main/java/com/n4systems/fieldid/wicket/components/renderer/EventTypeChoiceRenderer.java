package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.EventType;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class EventTypeChoiceRenderer implements IChoiceRenderer<EventType> {
    @Override
    public Object getDisplayValue(EventType eventType) {
        return eventType.getName();
    }

    @Override
    public String getIdValue(EventType eventType, int index) {
        return eventType.getId()+"";
    }
}
