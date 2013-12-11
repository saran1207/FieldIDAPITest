package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Transactional
public class EventTypeService extends FieldIdPersistenceService {

    public List<ActionEventType> getActionEventTypes(Long eventTypeGroupId) {
        return getActionEventTypes(eventTypeGroupId, null);
    }

    public List<ThingEventType> getThingEventTypes() {
        return getThingEventTypes(null, null);
    }

    public List<PlaceEventType> getPlaceEventTypes(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<PlaceEventType> query = createEventTypeQuery(PlaceEventType.class, eventTypeGroupId, nameFilter);
        return persistenceService.findAll(query);
    }

    public List<ThingEventType> getThingEventTypes(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<ThingEventType> query = createEventTypeQuery(ThingEventType.class, eventTypeGroupId, nameFilter);
        return persistenceService.findAll(query);
    }

    public List<ActionEventType> getActionEventTypes(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<ActionEventType> builder = createEventTypeQuery(ActionEventType.class, eventTypeGroupId, nameFilter);
        return persistenceService.findAll(builder);
    }

    public List<EventType> getAllEventTypes(Long eventTypeGroupId) {
        return getAllEventTypes(eventTypeGroupId, null);
    }

    public List<EventType> getAllEventTypes(Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<EventType> builder = createEventTypeQuery(EventType.class, eventTypeGroupId, nameFilter);
        return persistenceService.findAll(builder);
    }

    private <T extends EventType> QueryBuilder<T> createEventTypeQuery(Class<T> clazz, Long eventTypeGroupId, String nameFilter) {
        QueryBuilder<T> builder = createUserSecurityBuilder(clazz);

        if (eventTypeGroupId != null) {
            builder.addSimpleWhere("group.id", eventTypeGroupId);
        }

        if (!StringUtils.isBlank(nameFilter)) {
            builder.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH|WhereParameter.TRIM);
        }

        builder.addOrder("name");
        return builder;
    }

    public List<ThingEventType> getCommonEventTypesExcludingActions(List<AssetType> assetTypes) {
        List<ThingEventType> commonTypes = new ArrayList<ThingEventType>();
        Iterator<AssetType> iterator = assetTypes.iterator();
        if (iterator.hasNext()) {
            commonTypes.addAll(iterator.next().getAllEventTypesExcludingActions());
        }
        while (iterator.hasNext()) {
            List<ThingEventType> currentEventTypes = iterator.next().getAllEventTypesExcludingActions();
            for (Iterator<ThingEventType> commonTypesIterator = commonTypes.iterator(); commonTypesIterator.hasNext(); ) {
                EventType commonType = commonTypesIterator.next();
                if (!currentEventTypes.contains(commonType))
                    commonTypesIterator.remove();
            }
        }
        return commonTypes;
    }

    public void update(EventType type, User user) {
        type.setModified(new Date());
        type.setModifiedBy(user);
        persistenceService.update(type);
    }

	public void touchEventTypesForGroup(Long eventTypeGroupId, User modifiedBy) {
		List<EventType> eventTypes = getAllEventTypes(eventTypeGroupId, null);
		for (EventType eventType: eventTypes) {
			update(eventType, modifiedBy);
		}
	}

    public boolean hasEventTypes() {
        QueryBuilder<EventType> countEventTypes = createTenantSecurityBuilder(EventType.class);
        return persistenceService.count(countEventTypes) > 0;
    }
}
