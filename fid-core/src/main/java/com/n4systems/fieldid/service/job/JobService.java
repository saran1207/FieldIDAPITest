package com.n4systems.fieldid.service.job;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Project;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class JobService extends FieldIdPersistenceService {

    public List<Project> getActiveEventJobs() {
        QueryBuilder<Project> query = new QueryBuilder<Project>(Project.class, userSecurityFilter);
        query.addSimpleWhere("eventJob", true);
        query.addSimpleWhere("retired", false);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

}
