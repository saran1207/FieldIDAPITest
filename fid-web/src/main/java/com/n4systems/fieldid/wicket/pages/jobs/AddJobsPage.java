package com.n4systems.fieldid.wicket.pages.jobs;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Project;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AddJobsPage extends JobsFormPage {

    public AddJobsPage() {
        Project project = new Project();
        project.setTenant(getTenant());
        this.jobModel = Model.of(project);
    }

    @Override
    protected void onSave() {
        Project project = jobModel.getObject();
        project.setEventJob(jobType.equals(JobType.EVENT_JOB));
        if (project.getStatus() == null) {
            project.setStatus("");
        }
        projectService.save(project);
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.addjob"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("jobs.action").build(),
                aNavItem().label("nav.add").page(AddJobsPage.class).onRight().build()));
    }

}
