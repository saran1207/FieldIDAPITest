package com.n4systems.fieldid.service.schedule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

import java.util.List;

public class ScheduleService extends FieldIdPersistenceService {

    public List<EventSchedule> findIncompleteSchedulesForAsset(Asset asset) {
        QueryBuilder<EventSchedule> query = new QueryBuilder<EventSchedule>(EventSchedule.class, securityContext.getUserSecurityFilter());
        query.addWhere(WhereParameter.Comparator.NE, "status", "status", EventSchedule.ScheduleStatus.COMPLETED);
        query.addSimpleWhere("asset", asset);
        return persistenceService.findAll(query);
    }

}
