package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.tools.FileDataContainer;

public class PlaceEventCreationService extends EventCreationService<PlaceEvent, BaseOrg> {

    @Override
    protected PlaceEvent createEvent() {
        return null;
    }

    @Override
    protected void setTargetFromScheduleBundle(PlaceEvent event, EventScheduleBundle<BaseOrg> bundle) {
    }

    @Override
    protected void preUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
    }

    @Override
    protected void postUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
    }

}
