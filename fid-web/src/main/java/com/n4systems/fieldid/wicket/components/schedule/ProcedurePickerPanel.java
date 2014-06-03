package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureDefinition;
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
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class ProcedurePickerPanel extends Panel {

    private IModel<Procedure> scheduleModel;
    private ProcedureForm procedureForm;
    private WebMarkupContainer noActiveProcedureDefinitionMessage;
    private Label saveScheduleLabel;

    @SpringBean private ProcedureDefinitionService procedureDefinitionService;
    @SpringBean private ProcedureService procedureService;

    @SpringBean
    private DateService dateService;

    public ProcedurePickerPanel(String id, final IModel<Procedure> scheduleModel) {
        super(id);
        this.scheduleModel = scheduleModel;
        setOutputMarkupId(true);

        add(procedureForm = new ProcedureForm("procedureForm", scheduleModel));

        add(noActiveProcedureDefinitionMessage = new WebMarkupContainer("noActiveProcedureDefinitionMessage"));

        noActiveProcedureDefinitionMessage.add(new AjaxLink("authorLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new ProcedureDefinitionPage(scheduleModel.getObject().getAsset()));
            }
        });

        updateVisibility();
    }

    protected void updateVisibility() {
        Asset asset = scheduleModel.getObject().getAsset();

        Boolean isNew = scheduleModel.getObject().isNew();
        Boolean hasPublishedProcedureDef = procedureDefinitionService.hasPublishedProcedureDefinition(asset);

        noActiveProcedureDefinitionMessage.setVisible(!hasPublishedProcedureDef);
        if(isNew) {
            procedureForm.setVisible(hasPublishedProcedureDef);
        }

    }

    class ProcedureForm extends Form<Procedure> {

        FIDFeedbackPanel feedbackPanel;
        DateTimePicker dateTimePicker;

        public ProcedureForm(String id, IModel<Procedure> procedureScheduleModel) {
            super(id, procedureScheduleModel);

            Date dueDate = procedureScheduleModel.getObject().getDueDate();

            final List<ProcedureDefinition> choices = procedureDefinitionService.getAllPublishedProcedures(procedureScheduleModel.getObject().getAsset());

            setDefaultEventType(procedureScheduleModel, choices);

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            add(new DropDownChoice<ProcedureDefinition>("procedureDefSelect",
                    new PropertyModel<ProcedureDefinition>(procedureScheduleModel, "type"),
                    choices,
                    new ListableChoiceRenderer<ProcedureDefinition>())
                    .setNullValid(false)
                    .setRequired(true)
                    .add(new JChosenBehavior()));


            add(dateTimePicker = new DateTimePicker("datePicker", new PropertyModel<Date>(procedureScheduleModel, "dueDate"), true));
            dateTimePicker.getDateTextField().setRequired(true);
            dateTimePicker.setAllDay(dueDate == null || DateUtil.isMidnight(dueDate));

            ExaminersModel usersModel = new ExaminersModel();
            VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
            add(new AssignedUserOrGroupSelect("assignee",
                    ProxyModel.of(procedureScheduleModel, on(Procedure.class).getAssignedUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)).setRequired(true));

            AjaxSubmitLink addScheduleButton =  new AjaxSubmitLink("addScheduleButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onPickComplete(target);
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

        private void setDefaultEventType(IModel<Procedure> eventScheduleModel, List<ProcedureDefinition> eventTypeOptions) {
            Procedure eventSchedule = eventScheduleModel.getObject();
            if (eventSchedule.getType() == null && eventTypeOptions.size() > 0) {
                eventSchedule.setType(eventTypeOptions.get(0));
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
        response.renderCSSReference("style/legacy/newCss/component/wicket_procedure_picker.css");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        saveScheduleLabel.setDefaultModel(saveButtonLabel);
    }

}
