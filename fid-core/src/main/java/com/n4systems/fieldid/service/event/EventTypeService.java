package com.n4systems.fieldid.service.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.n4systems.model.AssetType;
import org.apache.commons.lang.StringUtils;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

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
            builder.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH|WhereParameter.TRIM);
        }

        builder.addOrder("name");

        return persistenceService.findAll(builder);
    }

    public List<EventType> getCommonEventTypes(List<AssetType> assetTypes) {
        List<EventType> commonTypes = new ArrayList<EventType>();
        Iterator<AssetType> iterator = assetTypes.iterator();
        if (iterator.hasNext()) {
            commonTypes.addAll(iterator.next().getAllEventTypes());
        }
        while (iterator.hasNext()) {
            List<EventType> currentEventTypes = iterator.next().getAllEventTypes();
            for (Iterator<EventType> commonTypesIterator = commonTypes.iterator(); commonTypesIterator.hasNext(); ) {
                EventType commonType = commonTypesIterator.next();
                if (!currentEventTypes.contains(commonType))
                    commonTypesIterator.remove();
            }
        }
        return commonTypes;
    }

}
