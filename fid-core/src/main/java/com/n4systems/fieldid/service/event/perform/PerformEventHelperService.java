package com.n4systems.fieldid.service.event.perform;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.util.NewEventTransientCriteriaResultPopulator;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;

@Transactional
public abstract class PerformEventHelperService<T extends Event, V extends EventType> extends FieldIdPersistenceService {

    private Class<T> eventClass;
    private Class<V> eventTypeClass;

    public PerformEventHelperService(Class<T> eventClass, Class<V> eventTypeClass) {
        this.eventClass = eventClass;
        this.eventTypeClass = eventTypeClass;
    }

    public T createEvent(Long assetId, Long typeId) {
        return createEvent(null, assetId, typeId);
    }

    public T createEvent(Long scheduleId, Long assetId, Long typeId) {
        return createEvent(scheduleId, assetId, typeId, false);
    }

    /**
     * Re-used Design LocalizeModel's and LocalizeAround's pattern with ThreadLocalInteractionContext to manage the user settings' language.
     * ThreadLocal are used for handle concurrency usage of languages
     * As a general pattern we store previous Language from ThreadLocalInteractionContext first (usually it is null)
     * Perform operations on the entity (event here) that are based on the user settings' language taken from SessionUser
     * Finally, language in the ThreadLocalInteractionContext is reverted back to its previous value
     * @param scheduleId
     * @param assetId
     * @param typeId
     * @param withLocalization
     * @return
     */
    public T createEvent(Long scheduleId, Long assetId, Long typeId, boolean withLocalization) {
        T event;

        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            if (withLocalization) {
                Locale language = getCurrentUser().getLanguage();
                ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(language);
            }
            if (scheduleId != null) {
                event = createEventFromOpenEvent(scheduleId);
                postFetchAdditionalFields(event);
                event = (T) event.clone();
                populateNewEvent(event);
            } else {
                event = createNewMasterEvent(assetId, typeId);
                populateNewEvent(event);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (withLocalization) {
                ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
            }
        }
        return event;
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
