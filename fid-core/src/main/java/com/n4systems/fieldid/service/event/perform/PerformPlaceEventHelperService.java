package com.n4systems.fieldid.service.event.perform;

import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.utils.PostFetcher;

public class PerformPlaceEventHelperService extends PerformEventHelperService<PlaceEvent, PlaceEventType>{

    public PerformPlaceEventHelperService() {
        super(PlaceEvent.class, PlaceEventType.class);
    }

    @Override
    protected PlaceEvent newEvent() {
        return new PlaceEvent();
    }

    @Override
    protected PlaceEvent createNewEvent(PlaceEvent event, Long targetId, Long eventTypeId) {
        PlaceEvent newEvent = super.createNewEvent(event, targetId, eventTypeId);
        event.setPlace(persistenceService.find(BaseOrg.class, targetId));
        return newEvent;
    }

    @Override
    protected void postFetchAdditionalFields(PlaceEvent event) {
        PostFetcher.postFetchFields(event, Event.PLACE_FIELD_PATHS);
    }
}
