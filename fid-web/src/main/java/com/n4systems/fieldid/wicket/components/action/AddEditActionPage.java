package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.date.DateService;
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
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Calendar;
import java.util.Date;

import static ch.lambdaj.Lambda.on;

public class AddEditActionPage extends FieldIDAuthenticatedPage {

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private DateService dateService;

    private boolean editMode = false;
    private boolean immediateSaveMode = false;
    private Class<? extends Event> eventClass;

    public AddEditActionPage(IModel<CriteriaResult> criteriaResultModel, Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
        Event event = null;
        try {
            event = eventClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        add(new AddActionForm("addActionForm", new Model<Event>(event), criteriaResultModel));
    }

    public AddEditActionPage(IModel<CriteriaResult> criteriaResultModel, IModel<Event> eventModel) {
        this.eventClass = (Class<Event>) eventModel.getObject().getClass();
        editMode = true;
        add(new AddActionForm("addActionForm", eventModel, criteriaResultModel));
    }

    class AddActionForm extends Form<Event> {
        public AddActionForm(String id, final IModel<Event> eventModel, final IModel<CriteriaResult> criteriaResultModel) {
            super(id, eventModel);
            setOutputMarkupId(true);

            final FIDFeedbackPanel feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
            add(feedbackPanel);

            IModel<Date> scheduledDateModel = ProxyModel.of(getModel(), on(Event.class).getDueDate());
            final DateTimePicker scheduledDatePicker = new DateTimePicker("dueDate", scheduledDateModel).withMonthsDisplayed(1).withNoAllDayCheckbox();
            scheduledDatePicker.getDateTextField().setRequired(true);

            add(scheduledDatePicker);

            UsersForTenantModel usersModel = new UsersForTenantModel();
            VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
            add(new AssignedUserOrGroupSelect("assignee",
                    ProxyModel.of(eventModel, on(Event.class).getAssignedUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)).setRequired(true));

            add(new DropDownChoice<EventType>("type", ProxyModel.of(getModel(), on(Event.class).getType()), new ActionTypesForTenantModel(), new ListableChoiceRenderer<EventType>()).setNullValid(true).setRequired(true));

            addQuickDateLinks(scheduledDatePicker, scheduledDateModel);

            DropDownChoice<PriorityCode> priorityChoice = new DropDownChoice<PriorityCode>("priority", ProxyModel.of(getModel(), on(Event.class).getPriority()), new PrioritiesForTenantModel(), new ListableChoiceRenderer<PriorityCode>());
            priorityChoice.setRequired(true);
            priorityChoice.setNullValid(true);
            priorityChoice.add(new UpdateComponentOnChange() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    if (getModelObject().isNew()) {
                        autoScheduleBasedOnPriority(getModel());
                        target.add(scheduledDatePicker);
                    }
                }
            });
            add(priorityChoice);

            TextArea<String> noteTextArea = new TextArea<String>("notes", ProxyModel.of(getModel(), on(Event.class).getNotes()));
            add(noteTextArea);
            noteTextArea.add(new StringValidator.MaximumLengthValidator(500));

            AjaxSubmitLink submitLink = new AjaxSubmitLink("submitLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (immediateSaveMode) {
                        persistenceService.update(getModelObject());
                        setResponsePage(new ActionDetailsPage(criteriaResultModel, eventClass, eventModel));

                    } else {
                        Event addedAction = getModelObject();

                        if (!editMode) {
                            addedAction.setTenant(FieldIDSession.get().getTenant());
                            criteriaResultModel.getObject().getActions().add(addedAction);
                        }

                        FieldIDSession.get().setActionsForCriteria(criteriaResultModel.getObject(), criteriaResultModel.getObject().getActions());
                        setResponsePage(new ActionsListPage(criteriaResultModel, eventClass));
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

            Link cancelLink = new  Link("cancelLink") {
                @Override public void onClick() {
                    setResponsePage(new ActionsListPage(criteriaResultModel, eventClass));
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
                    setResponsePage(new ActionsListPage(criteriaResultModel, eventClass));
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
            deleteLink.setVisible(editMode);
            add(deleteLink);
        }

        private void addQuickDateLinks(DateTimePicker scheduledDatePicker, IModel<Date> scheduledDateModel) {
            add(createQuickDateLink("todayLink", 0, scheduledDatePicker, scheduledDateModel));
            add(createQuickDateLink("tomorrowLink", 1, scheduledDatePicker, scheduledDateModel));
            add(createEndOfWeekLink("endOfWeekLink", scheduledDatePicker, scheduledDateModel));
            add(createQuickDateLink("in30DaysLink", 30, scheduledDatePicker, scheduledDateModel));
        }

        private AjaxLink createQuickDateLink(String id, final int deltaDays, final DateTimePicker scheduledDatePicker, final IModel<Date> scheduledDateModel) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date date = getDaysFromNow(deltaDays);
                    scheduledDateModel.setObject(new PlainDate(date));
                    target.add(scheduledDatePicker);
                }
            };
        }

        private AjaxLink createEndOfWeekLink(String id,final DateTimePicker scheduledDatePicker, final IModel<Date> scheduledDateModel) {
            return new AjaxLink(id) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    Date date = getEndOfWeek();
                    scheduledDateModel.setObject(new PlainDate(date));
                    target.add(scheduledDatePicker);
                }
            };
        }
    }

    private Date getDaysFromNow(int deltaDays) {
        Date date = dateService.todayAsDate();
        date = DateUtils.addDays(date, deltaDays);
        return date;
    }

    private void autoScheduleBasedOnPriority(IModel<Event> model) {
        PriorityCode priority = model.getObject().getPriority();
        if (priority != null && priority.getAutoSchedule() != null) {
            PriorityCodeAutoScheduleType autoSchedule = priority.getAutoSchedule();
            if (autoSchedule == PriorityCodeAutoScheduleType.END_OF_WEEK) {
                model.getObject().setDueDate(getEndOfWeek());
            } else if (autoSchedule == PriorityCodeAutoScheduleType.IN_30_DAYS) {
                model.getObject().setDueDate(getDaysFromNow(30));
            } else if (autoSchedule == PriorityCodeAutoScheduleType.TODAY) {
                model.getObject().setDueDate(getDaysFromNow(0));
            } else if (autoSchedule == PriorityCodeAutoScheduleType.TOMORROW) {
                model.getObject().setDueDate(getDaysFromNow(1));
            } else if (autoSchedule == PriorityCodeAutoScheduleType.CUSTOM && priority.getAutoScheduleCustomDays() != null) {
                model.getObject().setDueDate(getDaysFromNow(priority.getAutoScheduleCustomDays()));
            }
        }
    }

    private Date getEndOfWeek() {
        Date date = dateService.todayAsDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return calendar.getTime();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/event_actions.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/pages/alerts.css");
        response.renderCSSReference("style/newCss/layout/feedback_errors.css");
    }

    public void setImmediateSaveMode(boolean immediateSaveMode) {
        this.immediateSaveMode = immediateSaveMode;
    }

}
