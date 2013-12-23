package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.search.EventSearchType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventTypesForTenantModel extends FieldIDSpringModel<List<? extends EventType>> {

    @SpringBean private EventTypeService eventTypeService;

    private IModel<EventTypeGroup> eventTypeGroupModel;
    private IModel<EventSearchType> eventSearchTypeModel;

    public EventTypesForTenantModel() {
        this(new Model<EventTypeGroup>(null), new Model<EventSearchType>(EventSearchType.EVENTS));
    }

    public EventTypesForTenantModel(IModel<EventTypeGroup> eventTypeGroupModel) {
        this(eventTypeGroupModel, new Model<EventSearchType>(EventSearchType.EVENTS));
    }

    public EventTypesForTenantModel(IModel<EventTypeGroup> eventTypeGroupModel, IModel<EventSearchType> eventSearchTypeModel) {
        this.eventTypeGroupModel = eventTypeGroupModel;
        this.eventSearchTypeModel = eventSearchTypeModel;
    }

    @Override
    protected List<? extends EventType> load() {
        EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();
        Long groupId = eventTypeGroup == null ? null : eventTypeGroup.getId();
        if (eventSearchTypeModel.getObject() == EventSearchType.EVENTS) {
            return eventTypeService.getThingEventTypes(groupId, null);
        } else {
            return eventTypeService.getActionEventTypes(groupId, null);
        }
    }

}
