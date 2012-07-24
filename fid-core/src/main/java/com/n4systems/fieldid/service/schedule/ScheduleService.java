package com.n4systems.fieldid.service.schedule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class ScheduleService extends FieldIdPersistenceService {

    public List<Event> findIncompleteSchedulesForAsset(Asset asset) {
        QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, securityContext.getUserSecurityFilter());
        query.addSimpleWhere("eventState", Event.EventState.OPEN);
        query.addSimpleWhere("asset", asset);
        return persistenceService.findAll(query);
    }
}
