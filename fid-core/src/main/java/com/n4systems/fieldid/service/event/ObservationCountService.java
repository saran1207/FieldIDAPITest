package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ObservationCount;
import com.n4systems.model.ObservationCountCriteriaResult;
import com.n4systems.model.ObservationCountGroup;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class ObservationCountService extends FieldIdPersistenceService {

    public List<ObservationCountGroup> getObservationCountGroups() {
        return persistenceService.findAll(createTenantSecurityBuilder(ObservationCountGroup.class));
    }

    public Long countObservationCountGroups() {
        return persistenceService.count(createTenantSecurityBuilder(ObservationCountGroup.class));
    }

    public ObservationCountGroup saveOrUpdate(ObservationCountGroup group) {
        return persistenceService.saveOrUpdate(group);
    }

    public ObservationCountGroup archive(ObservationCountGroup group) {
        group.archiveEntity();
        return persistenceService.update(group);
    }

    public ObservationCountGroup unarchive(ObservationCountGroup group) {
        group.activateEntity();
        return persistenceService.update(group);
    }

    public void addObservationCount(ObservationCountGroup group, ObservationCount count) {
        persistenceService.save(count);
        group.getObservationCounts().add(count);
        saveOrUpdate(group);
    }

    public void updateObservationCount(ObservationCountGroup group, ObservationCount count) {
        persistenceService.update(count);
        saveOrUpdate(group);
    }

    public void archiveObservationCount(ObservationCountGroup group, ObservationCount count) {
        group.getObservationCounts().remove(count);
        persistenceService.archive(count);
        saveOrUpdate(group);
    }

    public ObservationCountCriteriaResult getObservationCountCriteriaResultByMobileId(String mobileId) {
        QueryBuilder<ObservationCountCriteriaResult> query = createUserSecurityBuilder(ObservationCountCriteriaResult.class);
        query.addSimpleWhere("mobileId", mobileId);

        return persistenceService.find(query);
    }

}
