package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.List;

public class PriorityCodeService extends FieldIdPersistenceService {

    public List<PriorityCode> getActivePriorityCodes() {
        QueryBuilder<PriorityCode> builder = createUserSecurityBuilder(PriorityCode.class);

        builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    public List<PriorityCode> getArchivedPriorityCodes() {
        QueryBuilder<PriorityCode> builder = createTenantSecurityBuilder(PriorityCode.class, true);

        builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ARCHIVED);

        return persistenceService.findAll(builder);
    }

    public void create(PriorityCode priorityCode) {
        persistenceService.save(priorityCode);
    }

    public void update(PriorityCode priorityCode) {
        persistenceService.update(priorityCode);
    }

    public void archive(PriorityCode priorityCode) {
        priorityCode.archiveEntity();
        update(priorityCode);
    }

    public void unarchive(PriorityCode priorityCode) {
        priorityCode.activateEntity();
        update(priorityCode);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<PriorityCode> builder = createUserSecurityBuilder(PriorityCode.class);
        builder.addWhere(WhereClauseFactory.create("name", name));
        if(id != null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE,  "id", id));
        }
        return persistenceService.exists(builder);
    }

    public PriorityCode getPriorityCodeByName(String name) {
        if (name==null) {
            return null;
        }
        QueryBuilder<PriorityCode> builder = createUserSecurityBuilder(PriorityCode.class);

        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        builder.addSimpleWhere("name", name);
        builder.addOrder("name");

        return persistenceService.find(builder);
    }

    public Long countOpenActionsWithPriorityCode(PriorityCode priorityCode) {
        return persistenceService.count(getOpenActionsByPriorityQuery(priorityCode));
    }

    public void archiveAndUpdateActions(PriorityCode priorityCode, PriorityCode newPriorityCode) {
        List<Event> openActions = persistenceService.findAll(getOpenActionsByPriorityQuery(priorityCode));
        for (Event action: openActions) {
            action.setPriority(newPriorityCode);
            persistenceService.update(action);
        }
        persistenceService.archive(priorityCode);
    }

    private QueryBuilder<Event> getOpenActionsByPriorityQuery(PriorityCode priorityCode) {
        QueryBuilder<Event> builder = createTenantSecurityBuilder(Event.class);
        builder.addSimpleWhere("type.actionType", true);
        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
        builder.addSimpleWhere("priority", priorityCode);
        return builder;
    }
}
