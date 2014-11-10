package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.services.date.DateService;
import com.n4systems.util.time.DateUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class SchedulePickerPanel<T extends Event> extends Panel {

    private IModel<T> scheduleModel;
    private ScheduleForm scheduleForm;
    private Label saveScheduleLabel;

    @SpringBean
    private DateService dateService;

    public SchedulePickerPanel(String id, IModel<T> scheduleModel, IModel<List<? extends EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        super(id);
        this.scheduleModel = scheduleModel;
        setOutputMarkupId(true);

        add(scheduleForm = new ScheduleForm<T>("scheduleForm", scheduleModel, new LocalizeModel<List<? extends EventType>>(eventTypeOptions), jobsOptions));
    }

    class ScheduleForm<T extends Event> extends Form {

        FIDFeedbackPanel feedbackPanel;
        DateTimePicker dateTimePicker;

        public ScheduleForm(String id, final IModel<T> eventScheduleModel, final IModel<List<? extends EventType>> eventTypeOptions, final IModel<List<Project>> jobsOptions) {
            super(id, eventScheduleModel);

            Date dueDate = eventScheduleModel.getObject().getDueDate();

            setDefaultEventType(eventScheduleModel, eventTypeOptions);

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(eventScheduleModel, "dueDate"), true) {
                @Override
                protected void onDatePicked(AjaxRequestTarget target) {
                    eventScheduleModel.getObject().setAssigneeOrDateUpdated();
                }
            });
            dateTimePicker.getDateTextField().setRequired(true);
            dateTimePicker.setAllDay(dueDate == null || DateUtil.isMidnight(dueDate));
            dateTimePicker.withoutPerformSetDateOnInitialization();

            DropDownChoice<EventType> eventTypeSelect = new FidDropDownChoice<EventType>("eventTypeSelect", new PropertyModel<EventType>(eventScheduleModel, "type"), eventTypeOptions, new ListableChoiceRenderer<EventType>());
            DropDownChoice<Project> jobSelect = new FidDropDownChoice<Project>("jobSelect", new PropertyModel<Project>(eventScheduleModel, "project"), jobsOptions, new ListableChoiceRenderer<Project>());
            jobSelect.setNullValid(true);

            eventTypeSelect.setNullValid(false);
            eventTypeSelect.setRequired(true);

            add(eventTypeSelect);
            WebMarkupContainer jobSelectContainer = new WebMarkupContainer("jobSelectContainer");
            jobSelectContainer.add(jobSelect);

            if (eventScheduleModel.getObject().getClass().equals(PlaceEvent.class)) {
                jobSelectContainer.setVisible(false);
            } else {
                jobSelectContainer.setVisible(FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Projects));
            }

            add(jobSelectContainer);

            add(new CheckBox("sendEmailOnUpdate", new PropertyModel<>(eventScheduleModel, "sendEmailOnUpdate")));

            //priority
            DropDownChoice<PriorityCode> priorityChoice = new FidDropDownChoice<PriorityCode>("priority", ProxyModel.of(getModel(), on(Event.class).getPriority()), new PrioritiesForTenantModel(), new ListableChoiceRenderer<PriorityCode>());
            add(priorityChoice);

            if ((null != eventScheduleModel.getObject().getType()) && (eventScheduleModel.getObject().getType().isActionEventType())) {
                priorityChoice.setVisible(true);

            } else {
                priorityChoice.setVisible(false);
            }

            ExaminersModel usersModel = new ExaminersModel();
            VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
            AssignedUserOrGroupSelect assignedUserOrGroupSelect = new AssignedUserOrGroupSelect("assignee",
                    ProxyModel.of(eventScheduleModel, on(Event.class).getAssignedUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel));

            if ((null != eventScheduleModel.getObject().getType()) && (eventScheduleModel.getObject().getType().isActionEventType())) {
                assignedUserOrGroupSelect.setNullVoid(false);
            }
            assignedUserOrGroupSelect.addBehavior(new UpdateComponentOnChange() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    eventScheduleModel.getObject().setAssigneeOrDateUpdated();
                }
            });

            add(assignedUserOrGroupSelect);

            AjaxSubmitLink addScheduleButton =  new AjaxSubmitLink("addScheduleButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onPickComplete(target);
                    setDefaultEventType(eventScheduleModel, eventTypeOptions);
                    target.add(feedbackPanel);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            };
            addScheduleButton.add(saveScheduleLabel = new Label("saveScheduleLabel", new FIDLabelModel("label.create_schedule")));

            add(addScheduleButton);

            add(createQuickDateLink("quickLinkToday", 0, 0, 0));
            add(createQuickDateLink("quickLinkTomorrow", 1, 0, 0));
            add(createQuickDateLink("quickLinkNextMonth", 0, 1, 0));
            add(createQuickDateLink("quickLinkSixMonths", 0, 6, 0));
            add(createQuickDateLink("quickLinkNextYear", 0, 0, 1));

            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ModalWindow.closeCurrent(target);
                }
            });
        }

        private void setDefaultEventType(IModel<T> eventScheduleModel, IModel<List<? extends EventType>> eventTypeOptions) {
            List<? extends EventType> availableEventTypes = eventTypeOptions.getObject();
            Event eventSchedule = eventScheduleModel.getObject();
            if (eventSchedule.getType() == null && availableEventTypes.size() > 0) {
                eventSchedule.setType(availableEventTypes.get(0));
            }
        }

        private AjaxLink createQuickDateLink(String id, final int daysFromNow, final int monthsFromNow, final int yearsFromNow) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date dateToSchedule = DateUtils.addYears(dateService.todayAsDate(), yearsFromNow);
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
        response.renderCSSReference("style/legacy/newCss/component/wicket_schedule_picker.css");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        saveScheduleLabel.setDefaultModel(saveButtonLabel);
    }

}
