package com.n4systems.fieldid.wicket.model.jobs;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.Project;

import java.util.List;

public class JobsForTenantModel extends FieldIDSpringModel<List<Project>> {

    @Override
    protected List<Project> load() {
        return null;
    }

}
