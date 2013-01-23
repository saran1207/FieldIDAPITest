package com.n4systems.fieldid.wicket.pages.massupdate;

import com.n4systems.fieldid.service.massupdate.MassUpdateService;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReturnToReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunLastReportPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Project;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

public class AssignEventsToJobPage extends FieldIDFrontEndPage {

    private IModel<EventReportCriteria> criteriaModel;

    @SpringBean
    private MassUpdateService massUpdateService;

    public AssignEventsToJobPage(IModel<EventReportCriteria> criteriaModel) {
        this.criteriaModel = criteriaModel;
        add(new AssignToJobForm("assignForm"));
    }

    class AssignToJobForm extends Form<AssignEventsToJobModel> {

        public AssignToJobForm(String id) {
            super(id, new Model<AssignEventsToJobModel>(new AssignEventsToJobModel()));
            PropertyModel<Project> projectModel = ProxyModel.of(getModel(), on(AssignEventsToJobModel.class).getJob());
            add(new DropDownChoice<Project>("job", projectModel, new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));
            add(new BookmarkablePageLink<Void>("returnToReportLink", ReturnToReportPage.class));
            add(new Button("assignButton"));
        }

        @Override
        protected void onSubmit() {
            Project job = getModelObject().getJob();

            massUpdateService.assignToJobs(criteriaModel.getObject().getSelection().getSelectedIds(), job);

            String message;
            if (job == null) {
                message = "message.eventscheduleremovedfromjobsuccessfully";
            } else {
                message = "message.eventscheduleassignedtojobsuccessfully";
            }

            getSession().info(new StringResourceModel(message, this, null,
                    new Object[] { criteriaModel.getObject().getSelection().getSelectedIds().size() }).getString());

            setResponsePage(RunLastReportPage.class);
        }
    }

    static class AssignEventsToJobModel implements IClusterable {
        public Project job;

        public Project getJob() {
            return job;
        }

        public void setJob(Project job) {
            this.job = job;
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.assigntojob"));
    }
}
