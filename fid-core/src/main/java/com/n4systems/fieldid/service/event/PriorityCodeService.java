package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PriorityCode;
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
}
