package com.n4systems.fieldid.wicket.pages.jobs;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.project.ProjectService;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.Project;
import com.n4systems.model.api.DisplayEnum;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public abstract class JobsFormPage extends FieldIDTemplatePage {

    @SpringBean
    protected ProjectService projectService;

    protected IModel<Project> jobModel;

    protected enum JobType implements DisplayEnum {
        EVENT_JOB("label.event_job"),
        ASSET_JOB("label.asset_job");

        private String label;

        private JobType(String label) {
            this.label = label;
        };

        @Override
        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    };

    protected JobType jobType = JobType.EVENT_JOB;

    public JobsFormPage() {}

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new JobsForm("form", jobModel));
    }

    protected abstract void onSave();

    private class JobsForm extends Form<Project> {

        private final OrgLocationPicker ownerPicker;

        public JobsForm(String id, IModel<Project> model) {
            super(id, model);

            add(new RadioChoice<>("jobType", new PropertyModel<>(JobsFormPage.this, "jobType"), Lists.newArrayList(JobType.values()), new IChoiceRenderer<JobType>() {
                @Override
                public Object getDisplayValue(JobType jobType) {
                    return new FIDLabelModel(jobType.getLabel()).getObject();
                }

                @Override
                public String getIdValue(JobType jobType, int index) {
                    return jobType.getName();
                }
            }).setVisible(model.getObject().isNew()));

            add(new RequiredTextField<>("jobID", new PropertyModel<>(model, "projectID"))
                    .add(new JobIDUniqueNameValidator(model.getObject().getId())));

            add(new RequiredTextField<>("title", new PropertyModel<>(model, "name")));

            add(ownerPicker = new OrgLocationPicker("ownerPicker", new PropertyModel<>(model, "owner")));

            add(new TextField<>("status", new PropertyModel<>(model, "status")));

            add(new CheckBox("isOpen", new PropertyModel<>(model, "open")));

            add(new TextArea<>("description", new PropertyModel<>(model, "description")));

            add(new DateTimeLabel("created", new UserToUTCDateModel(new PropertyModel<>(model, "created"))));

            add(new DateTimePicker("dateStartedPicker", new UserToUTCDateModel(new PropertyModel<>(model, "started")), true).withNoAllDayCheckbox().withoutPerformSetDateOnInitialization());

            add(new DateTimePicker("estimatedCompletionPicker", new UserToUTCDateModel(new PropertyModel<>(model, "estimatedCompletion")), true).withNoAllDayCheckbox().withoutPerformSetDateOnInitialization());

            add(new DateTimePicker("actualCompletionPicker", new UserToUTCDateModel(new PropertyModel<>(model, "actualCompletion")), true).withNoAllDayCheckbox().withoutPerformSetDateOnInitialization());

            add(new TextField<>("duration", new PropertyModel<>(model, "duration")));

            add(new TextField<>("poNumber", new PropertyModel<>(model, "poNumber")));

            add(new TextArea<>("workPerformed", new PropertyModel<>(model, "workPerformed")));

            add(new SubmitLink("saveLink"));

            add(new NonWicketLink("cancelLink", "jobs.action"));
        }

        @Override
        protected void onSubmit() {
            onSave();
            redirect("/jobs.action");
        }

        @Override
        protected void onValidate() {
            super.onValidate();
            if(ownerPicker.getTextString() == null || ownerPicker.getTextString().equals("")) {
                error(new FIDLabelModel("error.owner.required").getObject());
            }
        }




    }

    private class JobIDUniqueNameValidator extends AbstractValidator<String> {

        private Long id;

        public JobIDUniqueNameValidator(Long id) {
            this.id = id;
        }

        @Override
        protected void onValidate(IValidatable<String> validatable) {
            String projectID = validatable.getValue();

            if(projectService.exists(projectID, id)) {
                ValidationError error = new ValidationError().addMessageKey("errors.projectidduplicated");
                validatable.error(error);
            }

        }
    }

}
