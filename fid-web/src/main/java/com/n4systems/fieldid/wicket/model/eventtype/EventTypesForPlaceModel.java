package com.n4systems.fieldid.wicket.model.eventtype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.PlaceEventType;
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
    protected List<PlaceEventType> load() {

        if (orgModel.getObject() == null) {
            return Lists.newArrayList();
        }

        List<PlaceEventType> eventTypes = Lists.newArrayList(orgModel.getObject().getEventTypes());

        Collections.sort(eventTypes, new Comparator<EventType>() {
            @Override
            public int compare(EventType eventType, EventType eventType1) {
                return eventType.getName().compareTo(eventType1.getName());
            }
        });

        return eventTypes;
    }

}
