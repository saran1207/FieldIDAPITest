package com.n4systems.fieldid.service.remover;

import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.handlers.remover.summary.AssociatedPlaceEventTypeSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;

public class AssociatedPlaceEventTypeRemovalService extends FieldIdPersistenceService {

    @Autowired
    RecurringScheduleService recurringScheduleService;

    @Transactional
    public AssociatedPlaceEventTypeSummary summary(PlaceEventType eventType) {
        return new AssociatedPlaceEventTypeSummary(countPlaceAssociations(eventType), countPlaceRecurrences(eventType));
    }

    private Long countPlaceRecurrences(PlaceEventType eventType) {
        return persistenceService.count(getRecurringPlaceEventQuery(eventType));
    }

    private QueryBuilder<RecurringPlaceEvent> getRecurringPlaceEventQuery(EventType eventType) {
        QueryBuilder<RecurringPlaceEvent> query = createTenantSecurityBuilder(RecurringPlaceEvent.class);
        query.addSimpleWhere("eventType", eventType);
        return query;
    }

    private Long countPlaceAssociations(PlaceEventType eventType) {
        List result = getOrgAssociationQuery(eventType).getResultList();
        return Long.valueOf(result.size());
    }

    private Query getOrgAssociationQuery(EventType eventType) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        Maps.newHashMap();
        params.put("eventType", eventType);
        return persistenceService.createQuery("FROM " + BaseOrg.class.getName() + " o, IN (o.eventTypes) eventType WHERE eventType = :eventType", params);
    }

    @Transactional
    public void removeRecurringEvents(EventType eventType) {
        List<RecurringPlaceEvent> events = persistenceService.findAll(getRecurringPlaceEventQuery(eventType));
        for(RecurringPlaceEvent event: events) {
            event.retireEntity();
            persistenceService.update(event);
        }
    }

    @Transactional
    public void removeOrgConnections(EventType eventType) {
        List<Object[]> results = (List<Object[]>) getOrgAssociationQuery(eventType).getResultList();
        for (Object[] result: results) {
            BaseOrg org = (BaseOrg) result[0];
            org.getEventTypes().remove(eventType);
            persistenceService.update(org);
        }
    }
}
