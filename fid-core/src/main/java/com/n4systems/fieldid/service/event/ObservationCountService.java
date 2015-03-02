package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.*;

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

    public boolean isObservationGroupAttachedToEventType(Long observationGroupId) {
        QueryBuilder<EventForm> query = createUserSecurityBuilder(EventForm.class, true);
        query.addSimpleWhere("observationCountGroup.id", observationGroupId);

        WhereParameterGroup group = new WhereParameterGroup("group");
        group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "active", "state", Archivable.EntityState.ACTIVE, null, WhereClause.ChainOp.OR));
        group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "retired", "state", Archivable.EntityState.RETIRED, null, WhereClause.ChainOp.OR));
        query.addWhere(group);

        Long count = persistenceService.count(query);
        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

}
