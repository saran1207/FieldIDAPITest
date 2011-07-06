package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;
import com.n4systems.model.eventtype.EventTypeListLoader;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class EventTypesForTenantModel extends FieldIDSpringModel<List<EventType>> {

    private IModel<EventTypeGroup> eventTypeGroupModel;

    public EventTypesForTenantModel(IModel<EventTypeGroup> eventTypeGroupModel) {
        this.eventTypeGroupModel = eventTypeGroupModel;
    }

    @Override
    protected List<EventType> load() {
        EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();
        Long groupId = eventTypeGroup == null ? null : eventTypeGroup.getId();
        return new EventTypesByEventGroupIdLoader(getSecurityFilter()).setEventTypeGroupId(groupId).load();
    }

}
