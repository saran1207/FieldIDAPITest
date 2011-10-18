package com.n4systems.fieldid.wicket.data.jobs;

import com.n4systems.fieldid.service.job.JobService;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.Project;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

public class OpenJobsForUserDataProvider extends FieldIDDataProvider<Project> {

    @SpringBean
    private JobService jobService;

    @Override
    public Iterator<? extends Project> iterator(int first, int count) {
        return jobService.getAssignedToMeList(true, first, count).iterator();
    }

    @Override
    public int size() {
        return jobService.countAssignedToMe(true).intValue();
    }

    @Override
    public IModel<Project> model(Project project) {
        return new EntityModel<Project>(Project.class, project.getId());
    }

}
