package com.n4systems.fieldid.wicket.components.eventtype;

import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedEventTypePicker extends GroupedDropDownChoice<EventType, EventTypeGroup>{

    private boolean useJChosen;

    public GroupedEventTypePicker(String id, IModel<EventType> eventTypeIModel, IModel<List<? extends EventType>> eventTypesModel) {
        this(id, eventTypeIModel, eventTypesModel, true);
    }

    public GroupedEventTypePicker(String id, IModel<EventType> eventTypeIModel, IModel<List<? extends EventType>> eventTypesModel, boolean useJChosen) {
        super(id, eventTypeIModel, eventTypesModel, new EventTypeChoiceRenderer());
        setOutputMarkupId(true);
        this.useJChosen = useJChosen;
    }


    @Override
    protected EventTypeGroup getGroup(EventType choice) {
        return choice.getGroup();
    }

    @Override
    protected String getGroupLabel(EventTypeGroup group) {
        if (group == null) {
            return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString("label.ungrouped", this);
        }
        return group.getDisplayName();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (useJChosen) {
            add(new JChosenBehavior());
        }
    }
}
