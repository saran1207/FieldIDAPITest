package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class EventTypeGroupService extends FieldIdPersistenceService {

    public List<EventTypeGroup> getEventTypeGroupsExcludingActions() {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addSimpleWhere("action", false);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public List<EventTypeGroup> getEventTypeGroupsActionsOnly() {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addSimpleWhere("action", true);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }
}
