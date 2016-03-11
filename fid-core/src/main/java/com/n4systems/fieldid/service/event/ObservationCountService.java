package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ObservationCountService extends FieldIdPersistenceService {

    @Autowired
    private EventFormService eventFormService;

    public List<ObservationCountGroup> getObservationCountGroups() {
        return persistenceService.findAll(createTenantSecurityBuilder(ObservationCountGroup.class));
    }

    public Long countObservationCountGroups() {
        return persistenceService.count(createTenantSecurityBuilder(ObservationCountGroup.class));
    }

    public ObservationCountGroup saveOrUpdate(ObservationCountGroup group) {
        return persistenceService.saveOrUpdate(group);
    }

    public ObservationCountGroup retireObservationGroup(ObservationCountGroup group) {
        for (ObservationCount observationCount: group.getObservationCounts()) {
            observationCount.retireEntity();
            persistenceService.update(observationCount);
        }
        group.retireEntity();
        return saveOrUpdate(group);
    }

    public ObservationCountGroup retireObservationGroup(Long id) {
        ObservationCountGroup group = persistenceService.find(ObservationCountGroup.class, id);
        return retireObservationGroup(group);
    }

    public void addObservationCount(ObservationCountGroup group, ObservationCount count) {
        persistenceService.save(count);
        group.getObservationCounts().add(count);
        saveOrUpdate(group);
    }

    public ObservationCountCriteriaResult getObservationCountCriteriaResultByMobileId(String mobileId) {
        QueryBuilder<ObservationCountCriteriaResult> query = createUserSecurityBuilder(ObservationCountCriteriaResult.class);
        query.addSimpleWhere("mobileId", mobileId);

        return persistenceService.find(query);
    }

    public void updateEventFormsWithNewObservationGroup(Long oldGroupId, ObservationCountGroup newGroup) {
        QueryBuilder<EventType> query = createUserSecurityBuilder(EventType.class);
        query.addSimpleWhere("eventForm.observationCountGroup.id", oldGroupId);
        query.addPostFetchPaths("eventForm.sections");

        for (EventType eventType: persistenceService.findAll(query)) {
            EventForm updatedForm = new EventForm();
            updatedForm.setTenant(eventType.getTenant());
            updatedForm.setSections(eventType.getEventForm().getSections());
            updatedForm = eventFormService.copyEventFormSettings(eventType.getEventForm(), updatedForm);
            updatedForm.setObservationCountGroup(newGroup);
            eventFormService.saveNewEventFormAfterObservationChange(eventType.getId(), updatedForm);
        }
    }

    public boolean isObservationGroupAttachedToEventType(Long observationGroupId) {
        QueryBuilder<EventForm> query = createUserSecurityBuilder(EventForm.class);
        query.addSimpleWhere("observationCountGroup.id", observationGroupId);

        return persistenceService.count(query) > 0 ? true: false;
    }

}
