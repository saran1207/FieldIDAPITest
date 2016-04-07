package com.n4systems.fieldid.wicket.pages.jobs;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.Project;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class EditJobsPage extends JobsFormPage {

    private final long id;

    public EditJobsPage(PageParameters params) {
        id = params.get("uniqueID").toLong();
        jobModel = Model.of(projectService.findById(id));
    }

    @Override
    protected void onSave() {
        Project project = jobModel.getObject();
        if (project.getStatus() == null) {
            project.setStatus("");
        }

        projectService.update(jobModel.getObject());
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.editjob"));
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("jobs.action").build(),
                aNavItem().label("nav.view").page("job.action").params(PageParametersBuilder.uniqueId(id)).build(),
                aNavItem().label("nav.edit").page(EditJobsPage.class).params(PageParametersBuilder.uniqueId(id)).build(),
                aNavItem().label("nav.events").page("jobEvents.action").params(PageParametersBuilder.param("projectId", id)).build(),
                aNavItem().label("nav.notes").page("jobNotes.action").params(PageParametersBuilder.param("projectId", id)).build(),
                aNavItem().label("nav.add").page(AddJobsPage.class).onRight().build()));
    }

}
