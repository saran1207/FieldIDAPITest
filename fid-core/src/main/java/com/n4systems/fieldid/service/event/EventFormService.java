package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EventFormService extends FieldIdPersistenceService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void saveNewEventForm(Long eventTypeId, EventForm eventForm) {
        EventType eventType = persistenceService.find(EventType.class, eventTypeId);

        if (eventType.getEventForm() != null) {
            eventType.getEventForm().setState(Archivable.EntityState.RETIRED);
            eventForm.setScoreCalculationType(eventForm.getScoreCalculationType());
            eventForm.setFailRange(eventForm.getFailRange());
            eventForm.setPassRange(eventForm.getPassRange());
            persistenceService.update(eventType.getEventForm());
        }

        eventType.setEventForm(eventForm);
        eventType.incrementFormVersion();

        persistenceService.update(eventForm);
        persistenceService.update(eventType);
    }

}
