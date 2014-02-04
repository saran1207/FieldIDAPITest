package com.n4systems.fieldid.service.event.perform;

import com.n4systems.model.*;
import com.n4systems.persistence.utils.PostFetcher;

public class PerformThingEventHelperService extends PerformEventHelperService<ThingEvent, ThingEventType> {

    public PerformThingEventHelperService() {
        super(ThingEvent.class, ThingEventType.class);
    }

    @Override
    protected ThingEvent newEvent() {
        return new ThingEvent();
    }

    @Override
    public void populateNewEvent(ThingEvent masterEvent) {
        super.populateNewEvent(masterEvent);
        masterEvent.setAdvancedLocation(masterEvent.getAsset().getAdvancedLocation());
        masterEvent.setOwner(masterEvent.getAsset().getOwner());
        masterEvent.setProofTestInfo(new ProofTestInfo());
        masterEvent.setAssetStatus(masterEvent.getAsset().getAssetStatus());
    }

    @Override
    protected ThingEvent createNewEvent(ThingEvent event, Long targetId, Long eventTypeId) {
        ThingEvent newEvent = super.createNewEvent(event, targetId, eventTypeId);
        Asset asset = persistenceService.find(Asset.class, targetId);

        newEvent.setAssetStatus(asset.getAssetStatus());
        newEvent.setAsset(asset);
        return event;
    }

    @Override
    protected void postFetchAdditionalFields(ThingEvent event) {
        PostFetcher.postFetchFields(event, Event.ALL_FIELD_PATHS);
        if (event.getType().isThingEventType()) {
            PostFetcher.postFetchFields(event, Event.THING_TYPE_PATHS);
        }
    }
}
