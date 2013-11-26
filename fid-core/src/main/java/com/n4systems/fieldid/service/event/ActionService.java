package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class ActionService extends FieldIdPersistenceService {

    public List<ThingEventType> getActionTypes() {
        QueryBuilder<ThingEventType> query = createUserSecurityBuilder(ThingEventType.class);
        query.addSimpleWhere("group.action", true);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

}
