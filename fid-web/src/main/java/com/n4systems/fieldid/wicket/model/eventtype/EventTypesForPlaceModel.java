package com.n4systems.fieldid.wicket.model.eventtype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.model.IModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventTypesForPlaceModel extends FieldIDSpringModel<List<? extends EventType>> {

    private IModel<BaseOrg> orgModel;

    public EventTypesForPlaceModel(IModel<BaseOrg> orgModel) {
        this.orgModel = orgModel;
    }

    @Override
    protected List<ThingEventType> load() {
        List<ThingEventType> eventTypes = Lists.newArrayList();

        if (orgModel.getObject() == null) {
            return eventTypes;
        }
        //TODO update when model changes are complete
/*        for (AssociatedEventType associatedEventType : orgModel.getObject().getAssociatedEventTypes()) {
            eventTypes.add(associatedEventType.getEventType());
        }*/

        Collections.sort(eventTypes, new Comparator<EventType>() {
            @Override
            public int compare(EventType eventType, EventType eventType1) {
                return eventType.getName().compareTo(eventType1.getName());
            }
        });

        return eventTypes;
    }

}
