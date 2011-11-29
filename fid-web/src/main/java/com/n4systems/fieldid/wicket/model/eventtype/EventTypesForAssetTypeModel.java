package com.n4systems.fieldid.wicket.model.eventtype;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventTypesForAssetTypeModel extends FieldIDSpringModel<List<EventType>> {

    private IModel<AssetType> assetType;

    public EventTypesForAssetTypeModel(IModel<AssetType> assetType) {
        this.assetType = assetType;
    }

    @Override
    protected List<EventType> load() {
        List<EventType> eventTypes = new ArrayList<EventType>();

        for (AssociatedEventType associatedEventType : assetType.getObject().getAssociatedEventTypes()) {
            eventTypes.add(associatedEventType.getEventType());
        }

        Collections.sort(eventTypes, new Comparator<EventType>() {
            @Override
            public int compare(EventType eventType, EventType eventType1) {
                return eventType.getName().compareTo(eventType1.getName());
            }
        });

        return eventTypes;
    }

}
