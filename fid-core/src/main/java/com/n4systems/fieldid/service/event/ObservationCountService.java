package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.*;
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
            eventType.getEventForm().setObservationCountGroup(newGroup);
            eventFormService.saveNewEventFormAfterObservationChange(eventType.getEventForm(), eventType.getEventForm().getSections(), eventType);
        }
    }

}
