package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class EventTypeChoiceRenderer<T extends EventType> implements IChoiceRenderer<T> {
    @Override
    public Object getDisplayValue(T eventType) {
        return eventType.getName();
    }

    @Override
    public String getIdValue(T eventType, int index) {
        return eventType.getId()+"";
    }
}
