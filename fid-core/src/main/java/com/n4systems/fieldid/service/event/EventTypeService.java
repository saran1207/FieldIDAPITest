package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class EventTypeService extends FieldIdPersistenceService {

    public List<EventType> getEventTypes(Long eventTypeGroupId) {
        return getEventTypes(eventTypeGroupId, null);
    }

    public List<EventType> getEventTypes(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<EventType> builder = createUserSecurityBuilder(EventType.class);

        if (eventTypeGroupId != null) {
            builder.addSimpleWhere("group.id", eventTypeGroupId);
        }

        if (!StringUtils.isBlank(nameFilter)) {
            builder.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH);
        }

        builder.addOrder("name");

        return persistenceService.findAll(builder);
    }

}
