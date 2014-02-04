package com.n4systems.fieldid.wicket.components.eventtypegroup;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import java.util.List;

public class EventTypeGroupDropDownChoice extends DropDownChoice<EventTypeGroup> {

    public EventTypeGroupDropDownChoice(String id, IModel<EventTypeGroup> eventTypeGroupIModel, List<EventTypeGroup> choices) {
        super(id, eventTypeGroupIModel, choices);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new JChosenBehavior());
    }

}
