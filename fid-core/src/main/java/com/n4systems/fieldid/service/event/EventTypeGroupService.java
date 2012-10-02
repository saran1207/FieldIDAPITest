package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.Date;
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
    
    public Long getNumberOfAssociatedEventTypes(EventTypeGroup group) {
        QueryBuilder<EventType> eventTypeCountQuery = createUserSecurityBuilder(EventType.class);
        eventTypeCountQuery.addSimpleWhere("group", group);
        return persistenceService.count(eventTypeCountQuery);
    }

    public void update(EventTypeGroup group, User user) {
        group.setModified(new Date());
        group.setModifiedBy(user);
        persistenceService.update(group);
    }

    public void archive(EventTypeGroup group, User user) {
        group.archiveEntity();
        update(group, user);
    }
}
