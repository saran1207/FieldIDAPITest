package com.n4systems.fieldid.service.job;

import java.util.List;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Project;
import com.n4systems.util.persistence.QueryBuilder;

public class JobService extends FieldIdPersistenceService {

    public List<Project> getActiveEventJobs() {
        QueryBuilder<Project> query = createUserSecurityBuilder(Project.class);
        query.addSimpleWhere("eventJob", true);
        query.addSimpleWhere("retired", false);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

}
