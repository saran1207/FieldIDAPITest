package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class SchedulePicker extends Panel {

    private IModel<EventSchedule> scheduleModel;
    private int dx;
    private int dy;

    public SchedulePicker(String id, IModel<EventSchedule> scheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        this(id, scheduleModel, eventTypeOptions, jobsOptions, 0, 0);
    }

    public SchedulePicker(String id, IModel<EventSchedule> scheduleModel, IModel<List<EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions, int dx, int dy) {
        super(id);
        this.scheduleModel = scheduleModel;
        this.dx = dx;
        this.dy = dy;
        setOutputMarkupId(true);

        add(new ScheduleForm("scheduleForm", scheduleModel, eventTypeOptions, jobsOptions));
        add(CSSPackageResource.getHeaderContribution("style/newCss/component/wicket_schedule_picker.css"));
    }

    class ScheduleForm extends Form<EventSchedule> {

        FIDFeedbackPanel feedbackPanel;
        AjaxButton openDialogButton;
        WebMarkupContainer editorContainer;
        DateTimePicker dateTimePicker;

        public ScheduleForm(String id, final IModel<EventSchedule> eventScheduleModel, final IModel<List<EventType>> eventTypeOptions, final IModel<List<Project>> jobsOptions) {
            super(id, eventScheduleModel);

            add(openDialogButton = new AjaxButton("openDialogButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setEditorVisible(target, true);
                    target.appendJavascript("translate($('#"+editorContainer.getMarkupId()+"'), $('#"+openDialogButton.getMarkupId()+"'),"+dy+","+dx+")");
                }
            });

            setDefaultEventType(eventScheduleModel, eventTypeOptions);

            add(editorContainer = new WebMarkupContainer("scheduleEditorContainer"));
            editorContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            editorContainer.setOutputMarkupPlaceholderTag(true);
            editorContainer.setVisible(false);

            editorContainer.add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(eventScheduleModel, "nextStandardDate")));
            dateTimePicker.getDateTextField().setRequired(true);

            DropDownChoice<EventType> eventTypeSelect = new DropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(eventScheduleModel, "eventType"), eventTypeOptions, new ListableChoiceRenderer<EventType>());
            DropDownChoice<Project> jobSelect = new DropDownChoice<Project>("jobSelect", new PropertyModel<Project>(eventScheduleModel, "project"), jobsOptions, new ListableChoiceRenderer<Project>());

            eventTypeSelect.setNullValid(false);
            eventTypeSelect.setRequired(true);
            jobSelect.setNullValid(true);

            editorContainer.add(eventTypeSelect);
            WebMarkupContainer jobSelectRow = new WebMarkupContainer("jobSelectRow");
            jobSelectRow.add(jobSelect);
            jobSelectRow.setVisible(FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Projects));
            editorContainer.add(jobSelectRow);

            editorContainer.add(new AjaxButton("addScheduleButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onPickComplete(target);
                    setDefaultEventType(eventScheduleModel, eventTypeOptions);
                    setEditorVisible(target, false);
                    target.addComponent(feedbackPanel);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
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
                }
            });

        }

        private void setDefaultEventType(IModel<EventSchedule> eventScheduleModel, IModel<List<EventType>> eventTypeOptions) {
            List<EventType> availableEventTypes = eventTypeOptions.getObject();
            EventSchedule eventSchedule = eventScheduleModel.getObject();
            if (eventSchedule.getEventType() == null && availableEventTypes.size() > 0) {
                eventSchedule.setEventType(availableEventTypes.get(0));
            }
        }

        private void setEditorVisible(AjaxRequestTarget target, boolean visible) {
            editorContainer.setVisible(visible);
            target.addComponent(editorContainer);
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
                    target.addComponent(dateTimePicker);
                }
            };
        }
    }

    protected void onPickComplete(AjaxRequestTarget target) { }

}
