package com.n4systems.services;

import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventForm;
import com.n4systems.model.api.Archivable;
import com.n4systems.persistence.Transaction;

import javax.persistence.Query;

public class EventFormService {

    public void retireEventForm(Transaction tx, Long eventFormId) {
        EventForm eventForm = tx.getEntityManager().find(EventForm.class, eventFormId);
        eventForm.setState(Archivable.EntityState.ARCHIVED);
        tx.getEntityManager().merge(eventForm);
    }

}
