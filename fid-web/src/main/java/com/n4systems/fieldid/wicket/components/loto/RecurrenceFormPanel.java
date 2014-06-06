package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.TimeContainer;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.util.EnumPropertyChoiceRenderer;
import com.n4systems.model.Asset;
import com.n4systems.model.Recurrence;
import com.n4systems.model.RecurrenceTimeOfDay;
import com.n4systems.model.RecurrenceType;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.RecurringLotoEvent;
import com.n4systems.model.user.Assignable;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.validation.FormValidatorAdapter;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RecurrenceFormPanel extends Panel {

    @SpringBean
    private DateService dateService;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private RecurringScheduleService recurringScheduleService;

    private IModel<Asset> assetModel;

    public RecurrenceFormPanel(String id, IModel<Asset> assetModel) {
        super(id, assetModel);
        this.assetModel = assetModel;
        add(new RecurringEventsForm("form"));
    }

    protected class RecurringEventsForm extends Form implements IFormValidator {

        // private fields used to back form components.
        private RecurrenceTimeOfDay time = RecurrenceTimeOfDay.NINE_AM;
        private List<RecurrenceTimeOfDay> times = Lists.newArrayList(RecurrenceTimeOfDay.NINE_AM);

        private RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        private Date dateTime = dateService.nowInUsersTimeZone().toDate();
        private TimeContainer timePicker;
        private DateTimePicker dateTimepicker;
        private DropDownChoice<RecurrenceType> recurrenceTypeDropDown;
        private final FIDFeedbackPanel feedback;

        private ProcedureDefinition procedureDefinition;
        private List<ProcedureDefinition> procedureDefinitionsList;
        private Assignable assignee;

        public RecurringEventsForm(String id) {
            super(id);

            add(feedback = new FIDFeedbackPanel("feedbackPanel"));

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());

            procedureDefinitionsList = procedureDefinitionService.getAllPublishedProcedures(assetModel.getObject());

            final IChoiceRenderer<ProcedureDefinition> procedureDefRenderer = new IChoiceRenderer<ProcedureDefinition>() {
                @Override public Object getDisplayValue(ProcedureDefinition object) {
                    return new FIDLabelModel(object.getProcedureCode()).getObject();
                }
                @Override public String getIdValue(ProcedureDefinition object, int index) {
                    return object.getId().toString();
                }
            };

            final WebMarkupContainer inputContainer = new WebMarkupContainer("createContainer");
            inputContainer.setOutputMarkupId(true);

            dateTimepicker = new DateTimePicker("dateTime", new PropertyModel<Date>(this, "dateTime"),true).withMonthsDisplayed(1).withNoAllDayCheckbox();
            timePicker = createTimePicker();

            FidDropDownChoice procedureDefSelect;
            inputContainer.add(procedureDefSelect = new FidDropDownChoice<ProcedureDefinition>("procedureDef", new PropertyModel<ProcedureDefinition>(this, "procedureDefinition"), procedureDefinitionsList, procedureDefRenderer));
            procedureDefSelect.setRequired(true);
            procedureDefSelect.setNullValid(true);

            recurrenceTypeDropDown = new FidDropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(this, "type"), recurrences, new EnumPropertyChoiceRenderer<RecurrenceType>());
            inputContainer.add(recurrenceTypeDropDown);
            inputContainer.add(dateTimepicker.setOutputMarkupPlaceholderTag(true));
            inputContainer.add(timePicker);

            recurrenceTypeDropDown.setNullValid(false).setOutputMarkupId(true).add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    updateTimeComponents(recurrenceTypeDropDown.getModel().getObject());
                    target.add(timePicker, dateTimepicker);
                }
            });

            ExaminersModel usersModel = new ExaminersModel();
            VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
            AssignedUserOrGroupSelect assignedUserOrGroupSelect = new AssignedUserOrGroupSelect("assignee",
                    new PropertyModel<Assignable>(this, "assignee"),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel));
            assignedUserOrGroupSelect.setRequired(true);
            inputContainer.add(assignedUserOrGroupSelect);

            inputContainer.add(new AjaxSubmitLink("create") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    RecurringLotoEvent newEvent = new RecurringLotoEvent(procedureDefinition, assignee, createRecurrence());
                    newEvent.setTenant(procedureDefinition.getTenant());
                    recurringScheduleService.addRecurringEvent(newEvent);
                    resetForm();
                    onCreateRecurrence(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            }.add(new AjaxIndicatorAppender()));
            add(inputContainer);

            add(new FormValidatorAdapter(RecurringEventsForm.this));

            updateTimeComponents(type);
        }

        public Recurrence createRecurrence() {
            if ( type.requiresDate() ) {
                return new Recurrence(type).withDayAndTime(dateTime);
            } else if (type.canHaveMultipleTimes()) {
                return new Recurrence(type).withTimes(times);
            } else {
                return new Recurrence(type).withTime(time);
            }
        }

        private void resetForm() {
            time = RecurrenceTimeOfDay.NINE_AM;
            procedureDefinition = (procedureDefinitionsList.size()>0) ? procedureDefinitionsList.get(0) : null;
            type = RecurrenceType.MONTHLY_1ST;
        }

        private TimeContainer createTimePicker() {
            return new TimeContainer("timeContainer", new PropertyModel<RecurrenceTimeOfDay>(RecurringEventsForm.this, "time"), new PropertyModel<List<RecurrenceTimeOfDay>>(RecurringEventsForm.this, "times"));
        }

        private void updateTimeComponents(RecurrenceType recurrenceType) {
            dateTimepicker.setVisible(recurrenceType.requiresDate());
            timePicker.setVisible(!recurrenceType.requiresDate());
            timePicker.updateComponents(recurrenceType);
        }

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            List<FormComponent> formComponents = new ArrayList<FormComponent>();
            formComponents.add(recurrenceTypeDropDown);
            if (dateTimepicker.isVisible()) {
                formComponents.add(dateTimepicker.getDateTextField());
            }
            if (timePicker.isVisible()&& timePicker.getSingleTime().isVisible()) {
                formComponents.add(timePicker.getSingleTime());
            }
            if (timePicker.isVisible() && timePicker.getMultipleTime().isVisible()) {
                formComponents.add(timePicker.getMultipleTime());
            }
            return  formComponents.toArray(new FormComponent[formComponents.size()]);
        }

        @Override
        public void validate(Form form) {
            switch (recurrenceTypeDropDown.getModel().getObject()) {
                case DAILY:
                    if (timePicker.getMultipleTime().getConvertedInput().size()==0) {
                        form.error(getString("label.time.required"));
                    };
                    break;
                case WEEKLY_MONDAY:
                case WEEKLY_TUESDAY:
                case WEEKLY_WEDNESDAY:
                case WEEKLY_THURSDAY:
                case WEEKLY_FRIDAY:
                case WEEKLY_SATURDAY:
                case WEEKLY_SUNDAY:
                case MONTHLY_1ST:
                case MONTHLY_15TH:
                case MONTHLY_LAST:
                case BIWEEKLY_MONDAY:
                case BIWEEKLY_TUESDAY:
                case BIWEEKLY_WEDNESDAY:
                case BIWEEKLY_THURSDAY:
                case BIWEEKLY_FRIDAY:
                case BIWEEKLY_SATURDAY:
                case BIWEEKLY_SUNDAY:
                case WEEKDAYS:
                    if (timePicker.getSingleTime().getConvertedInput()==null) {
                        form.error(getString("label.time.required"));
                    };
                    break;
                case ANNUALLY:
                    if (dateTimepicker.getDateTextField().getConvertedInput()==null) {
                        form.error(getString("label.date.required"));
                    };
                    break;
            }
        }

        public RecurrenceTimeOfDay getTime() {
            return time;
        }

        public List<RecurrenceTimeOfDay> getTimes() {
            return times;
        }

        public RecurrenceType getType() {
            return type;
        }
    }

    protected void onCreateRecurrence(AjaxRequestTarget target) {}

}
