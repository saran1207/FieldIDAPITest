package com.n4systems.fieldid.service.job;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Project;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class JobService extends FieldIdPersistenceService {

    public List<Project> getActiveEventJobs() {
        QueryBuilder<Project> query = createUserSecurityBuilder(Project.class);
        query.addSimpleWhere("eventJob", true);
        query.addSimpleWhere("retired", false);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public Long countAssignedToMe(boolean onlyOpen) {
        return persistenceService.count(createJobQueryBuilder(true, onlyOpen));
    }

    public List<Project> getAssignedToMeList(boolean onlyOpen, Integer pageNumber, Integer pageSize) {
		return persistenceService.findAll(createJobQueryBuilder(true, onlyOpen), pageNumber, pageSize);
	}

    private QueryBuilder<Project> createJobQueryBuilder(boolean justAssignedToUser, boolean onlyOpen) {
        QueryBuilder<Project> qBuilder = createUserSecurityBuilder(Project.class);
        qBuilder.setSimpleSelect().addSimpleWhere("retired", false);
		if (justAssignedToUser) {
			qBuilder.addRequiredLeftJoin("resources", "resource");
			qBuilder.addWhere(new WhereParameter<Long>(WhereParameter.Comparator.EQ, "resourceId", "resource.id", securityContext.getUserSecurityFilter().getUserId(), null, true));
		}
		if (onlyOpen) {
			qBuilder.addSimpleWhere("open", true);
		}

        return qBuilder;
    }

}
