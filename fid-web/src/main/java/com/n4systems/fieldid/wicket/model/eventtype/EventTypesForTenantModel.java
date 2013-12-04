package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventTypesForTenantModel extends FieldIDSpringModel<List<ThingEventType>> {

    private IModel<EventTypeGroup> eventTypeGroupModel;
    @SpringBean private EventTypeService eventTypeService;

    public EventTypesForTenantModel() {
        this(new Model<EventTypeGroup>(null));
    }

    public EventTypesForTenantModel(IModel<EventTypeGroup> eventTypeGroupModel) {
        this.eventTypeGroupModel = eventTypeGroupModel;
    }

    @Override
    protected List<ThingEventType> load() {
        EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();
        Long groupId = eventTypeGroup == null ? null : eventTypeGroup.getId();
        return eventTypeService.getEventTypesExcludingActions(groupId, null);
    }

}
