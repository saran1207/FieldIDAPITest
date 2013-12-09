package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ActionEventType;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class ActionService extends FieldIdPersistenceService {

    public List<ActionEventType> getActionTypes() {
        QueryBuilder<ActionEventType> query = createUserSecurityBuilder(ActionEventType.class);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

}
