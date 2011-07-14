package com.n4systems.fieldid.wicket.model.jobs;

import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.Project;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class EventJobsForTenantModel extends FieldIDSpringModel<List<Project>> {

    @SpringBean
    private JobService jobService;

    @Override
    protected List<Project> load() {
        return jobService.getActiveEventJobs();
    }

}
