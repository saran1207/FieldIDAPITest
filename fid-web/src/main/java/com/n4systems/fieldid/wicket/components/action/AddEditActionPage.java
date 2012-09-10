package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Calendar;
import java.util.Date;

public class AddEditActionPage extends FieldIDAuthenticatedPage {

    private boolean editMode = false;

    public AddEditActionPage(IModel<CriteriaResult> criteriaResultModel) {
        add(new AddActionForm("addActionForm", new Model<Event>(new Event()), criteriaResultModel));
    }

    public AddEditActionPage(IModel<CriteriaResult> criteriaResultModel, IModel<Event> eventModel) {
        editMode = true;
        add(new AddActionForm("addActionForm", eventModel, criteriaResultModel));
    }

    class AddActionForm extends Form<Event> {
        public AddActionForm(String id, final IModel<Event> eventModel, final IModel<CriteriaResult> criteriaResultModel) {
            super(id, eventModel);
            setOutputMarkupId(true);

            final FIDFeedbackPanel feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
            add(feedbackPanel);

            PropertyModel<Date> scheduledDateModel = new PropertyModel<Date>(getModel(), "nextDate");
            DateTimePicker scheduledDatePicker = new DateTimePicker("nextDate", scheduledDateModel).withMonthsDisplayed(1).withNoAllDayCheckbox();

            add(scheduledDatePicker);
            add(new DropDownChoice<User>("assignee", new PropertyModel<User>(getModel(), "assignee"), new ExaminersModel(), new ListableChoiceRenderer<User>()).setNullValid(true).setRequired(true));
            add(new DropDownChoice<EventType>("type", new PropertyModel<EventType>(getModel(), "type"), new ActionTypesForTenantModel(), new ListableChoiceRenderer<EventType>()).setNullValid(true).setRequired(true));

            addQuickDateLinks(scheduledDatePicker, scheduledDateModel);

            DropDownChoice<PriorityCode> priorityChoice = new DropDownChoice<PriorityCode>("priority", new PropertyModel<PriorityCode>(getModel(), "priority"), new PrioritiesForTenantModel(), new ListableChoiceRenderer<PriorityCode>());
            priorityChoice.setRequired(true);
            priorityChoice.setNullValid(true);
            add(priorityChoice);

            TextArea<String> noteTextArea = new TextArea<String>("notes", new PropertyModel<String>(getModel(), "notes"));
            add(noteTextArea);
            noteTextArea.add(new StringValidator.MaximumLengthValidator(500));

            AjaxSubmitLink submitLink = new AjaxSubmitLink("submitLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Event addedAction = getModelObject();

                    if (!editMode) {
                        addedAction.setTenant(FieldIDSession.get().getTenant());
                        criteriaResultModel.getObject().getActions().add(addedAction);
                    }

                    FieldIDSession.get().setActionsForCriteria(criteriaResultModel.getObject(), criteriaResultModel.getObject().getActions());
                    setResponsePage(new ActionsListPage(criteriaResultModel));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(AddActionForm.this);
                }
            };
            add(submitLink);

            if (editMode) {
                submitLink.add(new FlatLabel("saveLabel", new FIDLabelModel("label.save")));
            } else {
                submitLink.add(new FlatLabel("saveLabel", new FIDLabelModel("label.create")));
            }

        }

        private void addQuickDateLinks(DateTimePicker scheduledDatePicker, PropertyModel<Date> scheduledDateModel) {
            add(createQuickDateLink("todayLink", 0, scheduledDatePicker, scheduledDateModel));
            add(createQuickDateLink("tomorrowLink", 1, scheduledDatePicker, scheduledDateModel));
            add(createEndOfWeekLink("endOfWeekLink", scheduledDatePicker, scheduledDateModel));
            add(createQuickDateLink("in30DaysLink", 30, scheduledDatePicker, scheduledDateModel));
        }

        private AjaxLink createQuickDateLink(String id, final int deltaDays, final DateTimePicker scheduledDatePicker, final PropertyModel<Date> scheduledDateModel) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date date = new Date();
                    date = DateUtils.addDays(date, deltaDays);
                    scheduledDateModel.setObject(new PlainDate(date));
                    target.add(scheduledDatePicker);
                }
            };
        }

        private AjaxLink createEndOfWeekLink(String id,final DateTimePicker scheduledDatePicker, final PropertyModel<Date> scheduledDateModel) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    scheduledDateModel.setObject(new PlainDate(calendar.getTime()));
                    target.add(scheduledDatePicker);
                }
            };
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/event_actions.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}
