package com.n4systems.fieldid.wicket.model.jobs;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.Project;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventJobsForTenantModel extends FieldIDSpringModel<List<Project>> {

    @SpringBean
    private PersistenceManager pm;

    @Override
    protected List<Project> load() {
        QueryBuilder<Project> query = new QueryBuilder<Project>(Project.class, getSecurityFilter());
        query.addSimpleWhere("eventJob", true);
        query.addSimpleWhere("retired", false);
        query.addOrder("name");
        return pm.findAll(query);
    }

}
