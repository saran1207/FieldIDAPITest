package com.n4systems.services;

import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;

public class EventTypeService {

    public void retireEventType(Long eventTypeId) {
        Transaction tx = PersistenceManager.startTransaction();

        EventType eventType = tx.getEntityManager().find(EventType.class, eventTypeId);
        eventType.setState(Archivable.EntityState.ARCHIVED);
        tx.getEntityManager().merge(eventType);

        tx.commit();
    }

}
