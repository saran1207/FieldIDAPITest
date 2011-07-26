package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class EventTypeService extends FieldIdPersistenceService {

    public List<EventType> getEventTypes(Long eventTypeGroupId) {
        QueryBuilder<EventType> builder = createUserSecurityBuilder(EventType.class);

        if (eventTypeGroupId != null) {
            builder.addSimpleWhere("group.id", eventTypeGroupId);
        }

        builder.addOrder("name");

        return persistenceService.findAll(builder);
    }

}
