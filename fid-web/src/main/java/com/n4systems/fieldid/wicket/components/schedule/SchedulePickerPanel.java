package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class SchedulePickerPanel extends Panel {

    private IModel<Event> scheduleModel;
    private ScheduleForm scheduleForm;
    private Label saveScheduleLabel;

    public SchedulePickerPanel(String id, IModel<Event> scheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        super(id);
        this.scheduleModel = scheduleModel;
        setOutputMarkupId(true);

        add(scheduleForm = new ScheduleForm("scheduleForm", scheduleModel, eventTypeOptions, jobsOptions));
    }

    class ScheduleForm extends Form<Event> {

        FIDFeedbackPanel feedbackPanel;
        public WebMarkupContainer editorContainer;
        DateTimePicker dateTimePicker;

        public ScheduleForm(String id, final IModel<Event> eventScheduleModel, final IModel<List<EventType>> eventTypeOptions, final IModel<List<Project>> jobsOptions) {
            super(id, eventScheduleModel);

            setDefaultEventType(eventScheduleModel, eventTypeOptions);

            add(editorContainer = new WebMarkupContainer("scheduleEditorContainer"));
            editorContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            editorContainer.setOutputMarkupPlaceholderTag(true);

            editorContainer.add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(eventScheduleModel, "dueDate"), true));
            dateTimePicker.getDateTextField().setRequired(true);

            DropDownChoice<EventType> eventTypeSelect = new FidDropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(eventScheduleModel, "type"), eventTypeOptions, new ListableChoiceRenderer<EventType>());
            DropDownChoice<Project> jobSelect = new FidDropDownChoice<Project>("jobSelect", new PropertyModel<Project>(eventScheduleModel, "project"), jobsOptions, new ListableChoiceRenderer<Project>());
            jobSelect.setNullValid(true);

            eventTypeSelect.setNullValid(false);
            eventTypeSelect.setRequired(true);

            editorContainer.add(eventTypeSelect);
            WebMarkupContainer jobSelectContainer = new WebMarkupContainer("jobSelectContainer");
            jobSelectContainer.add(jobSelect);
            jobSelectContainer.setVisible(FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Projects));

            editorContainer.add(jobSelectContainer);

            DropDownChoice<User> assigneeChoice = new FidDropDownChoice<User>("assignee", new PropertyModel<User>(eventScheduleModel, "assignee"), new ExaminersModel(), new ListableChoiceRenderer<User>());
            assigneeChoice.setNullValid(true);
            editorContainer.add(assigneeChoice);

            AjaxSubmitLink addScheduleButton =  new AjaxSubmitLink("addScheduleButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onPickComplete(target);
                    setDefaultEventType(eventScheduleModel, eventTypeOptions);
                    setEditorVisible(target, false);
                    target.add(feedbackPanel);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            };
            addScheduleButton.add(saveScheduleLabel = new Label("saveScheduleLabel", new FIDLabelModel("label.create_schedule")));

            editorContainer.add(addScheduleButton);

            editorContainer.add(createQuickDateLink("quickLinkToday", 0, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkTomorrow", 1, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextMonth", 0, 1, 0));
            editorContainer.add(createQuickDateLink("quickLinkSixMonths", 0, 6, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextYear", 0, 0, 1));

            editorContainer.add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ModalWindow.closeCurrent(target);
                }
            });
        }

        private void setDefaultEventType(IModel<Event> eventScheduleModel, IModel<List<EventType>> eventTypeOptions) {
            List<EventType> availableEventTypes = eventTypeOptions.getObject();
            Event eventSchedule = eventScheduleModel.getObject();
            if (eventSchedule.getType() == null && availableEventTypes.size() > 0) {
                eventSchedule.setType(availableEventTypes.get(0));
            }
        }

        public void setEditorVisible(AjaxRequestTarget target, boolean visible) {
            setEditorVisible(visible);
            target.add(editorContainer);
        }

        public void setEditorVisible(boolean visible) {
            editorContainer.setVisible(visible);
        }

        private AjaxLink createQuickDateLink(String id, final int daysFromNow, final int monthsFromNow, final int yearsFromNow) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date dateToSchedule = DateUtils.addYears(new Date(), yearsFromNow);
                    dateToSchedule = DateUtils.addMonths(dateToSchedule, monthsFromNow);
                    dateToSchedule = DateUtils.addDays(dateToSchedule, daysFromNow);
                    scheduleModel.getObject().setDueDate(dateToSchedule);
                    // We must clear the input in case we have some raw input in the date field that had a validation error.
                    dateTimePicker.clearInput();
                    dateTimePicker.setAllDay(true);
                    target.add(dateTimePicker);
                }
            };
        }
    }

    protected void onPickComplete(AjaxRequestTarget target) { }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/wicket_schedule_picker.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        saveScheduleLabel.setDefaultModel(saveButtonLabel);
    }

}
