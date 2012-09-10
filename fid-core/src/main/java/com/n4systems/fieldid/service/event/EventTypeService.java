package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Transactional
public class EventTypeService extends FieldIdPersistenceService {

    public List<EventType> getEventTypesIncludingActions(Long eventTypeGroupId) {
        return getEventTypesIncludingActions(eventTypeGroupId, null);
    }

    public List<EventType> getEventTypesExcludingActions(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<EventType> query = createEventTypeQuery(eventTypeGroupId, nameFilter);
        query.addSimpleWhere("group.action", false);
        return persistenceService.findAll(query);
    }

    public List<EventType> getEventTypesIncludingActions(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<EventType> builder = createEventTypeQuery(eventTypeGroupId, nameFilter);
        return persistenceService.findAll(builder);
    }

    private QueryBuilder<EventType> createEventTypeQuery(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<EventType> builder = createUserSecurityBuilder(EventType.class);

        if (eventTypeGroupId != null) {
            builder.addSimpleWhere("group.id", eventTypeGroupId);
        }

        if (!StringUtils.isBlank(nameFilter)) {
            builder.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH|WhereParameter.TRIM);
        }

        builder.addOrder("name");
        return builder;
    }

    public List<EventType> getCommonEventTypesExcludingActions(List<AssetType> assetTypes) {
        List<EventType> commonTypes = new ArrayList<EventType>();
        Iterator<AssetType> iterator = assetTypes.iterator();
        if (iterator.hasNext()) {
            commonTypes.addAll(iterator.next().getAllEventTypesExcludingActions());
        }
        while (iterator.hasNext()) {
            List<EventType> currentEventTypes = iterator.next().getAllEventTypesExcludingActions();
            for (Iterator<EventType> commonTypesIterator = commonTypes.iterator(); commonTypesIterator.hasNext(); ) {
                EventType commonType = commonTypesIterator.next();
                if (!currentEventTypes.contains(commonType))
                    commonTypesIterator.remove();
            }
        }
        return commonTypes;
    }

}
