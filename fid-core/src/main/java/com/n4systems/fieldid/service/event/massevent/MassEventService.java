package com.n4systems.fieldid.service.event.massevent;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ThingEvent;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.List;

public class MassEventService extends FieldIdPersistenceService {

    public List<SelectedEventTypeCount> countSelectedEventTypes(List<Long> selectedEvents) {
        QueryBuilder<SelectedEventTypeCount> query = new QueryBuilder<SelectedEventTypeCount>(ThingEvent.class, securityContext.getUserSecurityFilter());
        query.setSelectArgument(new NewObjectSelect(SelectedEventTypeCount.class, "obj.type", "COUNT(*)"));
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "id", selectedEvents));
        query.addGroupBy("type");
        return persistenceService.findAll(query);
    }

}
