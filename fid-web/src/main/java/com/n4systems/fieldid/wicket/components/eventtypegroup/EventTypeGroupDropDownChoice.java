package com.n4systems.fieldid.wicket.components.eventtypegroup;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.model.IModel;

import java.util.List;

public class EventTypeGroupDropDownChoice extends GroupedDropDownChoice<EventTypeGroup, String> {


    public EventTypeGroupDropDownChoice(String id, IModel<EventTypeGroup> eventTypeGroupIModel, List<EventTypeGroup> choices) {
        super(id, eventTypeGroupIModel, choices);
    }

    @Override
    protected String getGroup(EventTypeGroup choice) {
        return choice.isAction() ? "label.actions" : "label.eventtypegroups";
    }

    @Override
    protected String getGroupLabel(String group) {
        return new FIDLabelModel(group).getObject();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new JChosenBehavior());
    }
}
