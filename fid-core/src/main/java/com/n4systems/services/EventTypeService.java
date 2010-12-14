package com.n4systems.services;

import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.persistence.Transaction;

import javax.persistence.Query;

public class EventTypeService {

    public void retireEventType(Transaction tx, Long eventTypeId) {
        EventType eventType = tx.getEntityManager().find(EventType.class, eventTypeId);
        eventType.setState(Archivable.EntityState.ARCHIVED);
        tx.getEntityManager().merge(eventType);
    }

    public void pointOldAssociationsToNewEventType(Transaction tx, Long oldEventTypeId, Long newEventTypeId) {
        Query query = tx.getEntityManager().createQuery("update " + AssociatedEventType.class.getName() + " aet set aet.eventType.id = :newEventTypeId where aet.eventType.id = :oldEventTypeId");;
        query.setParameter("oldEventTypeId", oldEventTypeId);
        query.setParameter("newEventTypeId", newEventTypeId);
        query.executeUpdate();
    }

}
