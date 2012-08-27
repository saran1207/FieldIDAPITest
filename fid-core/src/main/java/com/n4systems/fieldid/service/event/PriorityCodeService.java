package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class PriorityCodeService extends FieldIdPersistenceService {

    public List<PriorityCode> getActivePriorityCodes() {
        QueryBuilder<PriorityCode> builder = createUserSecurityBuilder(PriorityCode.class);

        builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    public void create(PriorityCode priorityCode) {
        persistenceService.save(priorityCode);
    }

    public void update(PriorityCode priorityCode) {
        persistenceService.update(priorityCode);
    }

}
