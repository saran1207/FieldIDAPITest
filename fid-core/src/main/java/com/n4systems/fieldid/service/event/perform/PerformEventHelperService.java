package com.n4systems.fieldid.service.event.perform;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.util.NewEventTransientCriteriaResultPopulator;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public abstract class PerformEventHelperService<T extends Event, V extends EventType> extends FieldIdPersistenceService {

    private Class<T> eventClass;
    private Class<V> eventTypeClass;

    public PerformEventHelperService(Class<T> eventClass, Class<V> eventTypeClass) {
        this.eventClass = eventClass;
        this.eventTypeClass = eventTypeClass;
    }

    public T createEvent(Long scheduleId, Long assetId, Long typeId) {
        try {
            T event;
            if (scheduleId != null) {
                event = createEventFromOpenEvent(scheduleId);
                postFetchAdditionalFields(event);
                event = (T) event.clone();
                populateNewEvent(event);
            } else {
                event = createNewMasterEvent(assetId, typeId);
                populateNewEvent(event);
            }
            return event;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void postFetchAdditionalFields(T event) {}

    public T createNewMasterEvent(Long targetId, Long eventTypeId) {
        return createNewEvent(newEvent(), targetId, eventTypeId);
    }

    public T createEventFromOpenEvent(Long openEventId) {
        return persistenceService.find(eventClass, openEventId);
    }

    public void populateNewEvent(T masterEvent) {
        masterEvent.setEventForm(masterEvent.getType().getEventForm());
        masterEvent.setDate(new Date());
        masterEvent.setPerformedBy(getCurrentUser());
        masterEvent.setPrintable(masterEvent.getEventType().isPrintable());
        masterEvent.setPerformedBy(getCurrentUser());
        masterEvent.setInitialResultBasedOnScoreOrOneClicksBeingAvailable();
        new NewEventTransientCriteriaResultPopulator().populateTransientCriteriaResultsForEvent(masterEvent);
    }

    @Transactional
    protected T createNewEvent(T event, Long targetId, Long eventTypeId) {
        EventType eventType = persistenceService.find(eventTypeClass, eventTypeId);

        event.setTenant(getCurrentTenant());
        event.setType(eventType);
        event.setEventForm(eventType.getEventForm());

        return event;
    }

    protected abstract T newEvent();

}
