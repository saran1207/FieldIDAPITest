package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class SchedulePicker extends Panel {

    private IModel<Event> scheduleModel;
    private ScheduleForm scheduleForm;
    AjaxButton openDialogButton;

    public SchedulePicker(String id, IModel<String> openPickerLabel, IModel<Event> scheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions, final int dx, final int dy) {
        super(id);
        this.scheduleModel = scheduleModel;
        setOutputMarkupId(true);

        Form openForm = new Form("openForm");
        openForm.add(openDialogButton = new AjaxButton("openDialogButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                scheduleForm.setEditorVisible(target, true);
                onPickerOpened(target);
                target.appendJavaScript("translateWithin($('#" + scheduleForm.editorContainer.getMarkupId() + "'), $('#" + openDialogButton.getMarkupId() + "'), $('#pageContent'), " + dy + ", " + dx + ");");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

            @Override
            public boolean isEnabled() {
                return super.isEnabled() && isOpenPickerButtonEnabled();
            }
        });
        add(openForm);
        openDialogButton.setOutputMarkupId(true);
        openDialogButton.add(new AttributeModifier("value", openPickerLabel.getObject()));
        openDialogButton.setEnabled(eventTypeOptions.getObject().size() > 0);
        add(scheduleForm = new ScheduleForm("scheduleForm", scheduleModel, eventTypeOptions, jobsOptions));
    }

    class ScheduleForm extends Form<Event> {

        FIDFeedbackPanel feedbackPanel;
        AjaxButton openDialogButton;
        public WebMarkupContainer editorContainer;
        DateTimePicker dateTimePicker;

        public ScheduleForm(String id, final IModel<Event> eventScheduleModel, final IModel<List<EventType>> eventTypeOptions, final IModel<List<Project>> jobsOptions) {
            super(id, eventScheduleModel);


            setDefaultEventType(eventScheduleModel, eventTypeOptions);

            add(editorContainer = new WebMarkupContainer("scheduleEditorContainer"));
            editorContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            editorContainer.setVisible(false);
            editorContainer.setOutputMarkupPlaceholderTag(true);

            editorContainer.add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(eventScheduleModel, "nextDate"), true));
            dateTimePicker.getDateTextField().setRequired(true);

            DropDownChoice<EventType> eventTypeSelect = new DropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(eventScheduleModel, "type"), eventTypeOptions, new ListableChoiceRenderer<EventType>());
            DropDownChoice<Project> jobSelect = new DropDownChoice<Project>("jobSelect", new PropertyModel<Project>(eventScheduleModel, "project"), jobsOptions, new ListableChoiceRenderer<Project>());
            jobSelect.setNullValid(true);

            eventTypeSelect.setNullValid(false);
            eventTypeSelect.setRequired(true);

            editorContainer.add(eventTypeSelect);
            WebMarkupContainer jobSelectContainer = new WebMarkupContainer("jobSelectContainer");
            jobSelectContainer.add(jobSelect);
            jobSelectContainer.setVisible(FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Projects));

            editorContainer.add(jobSelectContainer);

            DropDownChoice<User> assigneeChoice = new DropDownChoice<User>("assignee", new PropertyModel<User>(eventScheduleModel, "assignee"), new ExaminersModel(), new ListableChoiceRenderer<User>());
            assigneeChoice.setNullValid(true);
            editorContainer.add(assigneeChoice);

            editorContainer.add(new AjaxSubmitLink("addScheduleButton") {
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
            });

            editorContainer.add(createQuickDateLink("quickLinkToday", 0, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkTomorrow", 1, 0, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextMonth", 0, 1, 0));
            editorContainer.add(createQuickDateLink("quickLinkSixMonths", 0, 6, 0));
            editorContainer.add(createQuickDateLink("quickLinkNextYear", 0, 0, 1));

            editorContainer.add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setEditorVisible(target, false);
                    onPickerClosed(target);
                }
            });

            WebMarkupContainer closeImage = new WebMarkupContainer("closeImage");
            editorContainer.add(closeImage);
            closeImage.add(new AjaxEventBehavior("onclick") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    setEditorVisible(target, false);
                    onPickerClosed(target);
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
            editorContainer.setVisible(visible);
            target.add(editorContainer);
        }

        private AjaxLink createQuickDateLink(String id, final int daysFromNow, final int monthsFromNow, final int yearsFromNow) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date dateToSchedule = DateUtils.addYears(new Date(), yearsFromNow);
                    dateToSchedule = DateUtils.addMonths(dateToSchedule, monthsFromNow);
                    dateToSchedule = DateUtils.addDays(dateToSchedule, daysFromNow);
                    scheduleModel.getObject().setNextDate(dateToSchedule);
                    // We must clear the input in case we have some raw input in the date field that had a validation error.
                    dateTimePicker.clearInput();
                    dateTimePicker.setAllDay(true);
                    target.add(dateTimePicker);
                }
            };
        }
    }

    protected void onPickerOpened(AjaxRequestTarget target) { }
    protected void onPickerClosed(AjaxRequestTarget target) { }
    protected void onPickComplete(AjaxRequestTarget target) { }

    public void redrawOpenDialogButton(AjaxRequestTarget target) {
        target.add(scheduleForm.openDialogButton);
    }

    protected boolean isOpenPickerButtonEnabled() {
        return true;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/wicket_schedule_picker.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }


}
