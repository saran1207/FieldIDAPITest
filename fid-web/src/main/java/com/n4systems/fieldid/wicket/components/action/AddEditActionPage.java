package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.UserGroupsModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Calendar;
import java.util.Date;

import static ch.lambdaj.Lambda.on;

public class AddEditActionPage extends FieldIDAuthenticatedPage {

    @SpringBean
    private PersistenceService persistenceService;

    private boolean editMode = false;
    private boolean immediateSaveMode = false;

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

            PropertyModel<Date> scheduledDateModel = new PropertyModel<Date>(getModel(), "dueDate");
            DateTimePicker scheduledDatePicker = new DateTimePicker("dueDate", scheduledDateModel).withMonthsDisplayed(1).withNoAllDayCheckbox();
            scheduledDatePicker.getDateTextField().setRequired(true);

            add(scheduledDatePicker);

            ExaminersModel usersModel = new ExaminersModel();
            UserGroupsModel userGroupsModel = new UserGroupsModel();
            add(new AssignedUserOrGroupSelect("assignee",
                    ProxyModel.of(eventModel, on(Event.class).getAssignedUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)).setRequired(true));

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
                    if (immediateSaveMode) {
                        persistenceService.update(getModelObject());
                        setResponsePage(new ActionDetailsPage(criteriaResultModel, eventModel));

                    } else {
                        Event addedAction = getModelObject();

                        if (!editMode) {
                            addedAction.setTenant(FieldIDSession.get().getTenant());
                            criteriaResultModel.getObject().getActions().add(addedAction);
                        }

                        FieldIDSession.get().setActionsForCriteria(criteriaResultModel.getObject(), criteriaResultModel.getObject().getActions());
                        setResponsePage(new ActionsListPage(criteriaResultModel));
                    }
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

            Link cancelLink = new Link("cancelLink") {
                @Override public void onClick() {
                    setResponsePage(new ActionsListPage(criteriaResultModel));
                }
            };
            add(cancelLink);

            AjaxSubmitLink deleteLink = new AjaxSubmitLink("deleteLink") {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Event deletedAction = getModelObject();
                    criteriaResultModel.getObject().getActions().remove(deletedAction);

                    if(editMode && deletedAction.getId() != null) {
                        deletedAction.archiveEntity();
                        persistenceService.update(deletedAction);
                    }

                    FieldIDSession.get().setActionsForCriteria(criteriaResultModel.getObject(), criteriaResultModel.getObject().getActions());
                    setResponsePage(new ActionsListPage(criteriaResultModel));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(AddActionForm.this);
                }
            };
            
            if(editMode) {
                deleteLink.add(new Behavior() {
                    @Override
                    public void onComponentTag(Component component, ComponentTag tag) {
                        tag.put("onclick", "if(!confirm('" + new FIDLabelModel("label.confirm_delete_action").getObject() +"')) {return false;} " + tag.getAttribute("onclick"));
                    }
                });
            }
            add(deleteLink);
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

                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    }

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

    public void setImmediateSaveMode(boolean immediateSaveMode) {
        this.immediateSaveMode = immediateSaveMode;
    }

}
