package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.user.User;
import com.n4systems.services.tenant.TenantCreationService;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class EventTypeGroupService extends FieldIdPersistenceService {

	@Autowired private EventTypeService eventTypeService;

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

    public EventTypeGroup update(EventTypeGroup group, User user) {
        group.setModified(new Date());
        group.setModifiedBy(user);
		eventTypeService.touchEventTypesForGroup(group.getId(), user);
        return persistenceService.update(group);
    }

    public void archive(EventTypeGroup group, User user) {
        group.archiveEntity();
        update(group, user);
    }

    public EventTypeGroup getDefaultActionGroup() {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addSimpleWhere("action", true);
        // CAVEAT : assumes tenant_id/name=Actions is unique.
        query.addSimpleWhere("name", TenantCreationService.DEFAULT_ACTIONS_GROUP_NAME);
        return persistenceService.find(query);
    }
}
