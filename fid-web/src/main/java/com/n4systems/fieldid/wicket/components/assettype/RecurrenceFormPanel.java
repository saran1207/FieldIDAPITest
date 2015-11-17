package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.TimeContainer;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.components.renderer.EnumPropertyChoiceRenderer;
import com.n4systems.model.EventType;
import com.n4systems.model.Recurrence;
import com.n4systems.model.RecurrenceTimeOfDay;
import com.n4systems.model.RecurrenceType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.FormValidatorAdapter;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class RecurrenceFormPanel<T> extends Panel {

    @SpringBean
    private DateService dateService;

    private IModel<T> model;

    public RecurrenceFormPanel(String id, IModel<T> model) {
        super(id, model);
        add(new RecurringEventsForm("form"));
    }

    protected class RecurringEventsForm extends Form implements IFormValidator {

        // private fields used to back form components.
        private RecurrenceTimeOfDay time = RecurrenceTimeOfDay.NINE_AM;
        private List<RecurrenceTimeOfDay> times = Lists.newArrayList(RecurrenceTimeOfDay.NINE_AM);
        private EventType eventType = null;
        private RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        private BaseOrg owner;
        private Date dateTime = dateService.nowInUsersTimeZone().toDate();
        private TimeContainer timePicker;
        private DateTimePicker dateTimepicker;
        private DropDownChoice<RecurrenceType> recurrenceTypeDropDown;
        private final FIDFeedbackPanel feedback;
        private Boolean ownerAndDown;
        private Boolean autoassign;
        List<EventType> eventTypes;

        public RecurringEventsForm(String id) {
            super(id);

            add(feedback = new FIDFeedbackPanel("feedbackPanel"));

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());

            eventTypes = Lists.newArrayList(getEventTypes());
            // set default value if one available.
            eventType = (eventTypes.size()>0) ? eventTypes.get(0) : null;

            final IChoiceRenderer<EventType> eventTypeRenderer = new IChoiceRenderer<EventType>() {
                @Override public Object getDisplayValue(EventType object) {
                    return new FIDLabelModel(object.getName()).getObject();
                }
                @Override public String getIdValue(EventType object, int index) {
                    return object.getId().toString();
                }
            };

            final WebMarkupContainer inputContainer = new WebMarkupContainer("createContainer");
            inputContainer.setOutputMarkupId(true);

            dateTimepicker = new DateTimePicker("dateTime", new PropertyModel<Date>(this, "dateTime"),true).withMonthsDisplayed(1).withNoAllDayCheckbox();
            timePicker = createTimePicker();

            inputContainer.add(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(this, "eventType"), eventTypes, eventTypeRenderer).setNullValid(false));
            recurrenceTypeDropDown = new FidDropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(this, "type"), recurrences, new EnumPropertyChoiceRenderer<RecurrenceType>());
            inputContainer.add(recurrenceTypeDropDown);
            inputContainer.add(dateTimepicker.setOutputMarkupPlaceholderTag(true));
            inputContainer.add(timePicker);

            inputContainer.add(new OrgLocationPicker("organization", new PropertyModel<BaseOrg>(this, "owner")) {
                @Override public boolean isVisible() {
                    return isOrgPickerVisible();
                }
            });

            inputContainer.add(new CheckBox("ownerAndDown", new PropertyModel<Boolean>(this, "ownerAndDown")) {
                @Override public boolean isVisible() {
                    return isOwnerDownOptionVisible();
                }
            });

            inputContainer.add(new CheckBox("autoassign", new PropertyModel<Boolean>(this, "autoassign")) {
                @Override public boolean isVisible() {
                    return isAutoAssignVisible();
                }
            });

            ContextImage tooltip;
            inputContainer.add(tooltip = new ContextImage("tooltip", "images/tooltip-icon.png"));
            tooltip.add(new TipsyBehavior(new FIDLabelModel("message.recurring_events_owner_and_down"), TipsyBehavior.Gravity.N));


            ContextImage autotooltip;
            inputContainer.add(autotooltip = new ContextImage("autotooltip", "images/tooltip-icon.png"));
            autotooltip.add(new TipsyBehavior(new FIDLabelModel("message.recurring_events_auto_assign"), TipsyBehavior.Gravity.N));

            recurrenceTypeDropDown.setNullValid(false).setOutputMarkupId(true).add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    updateTimeComponents(recurrenceTypeDropDown.getModel().getObject());
                    target.add(timePicker, dateTimepicker);
                }
            });

            inputContainer.add(new AjaxSubmitLink("create") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onCreate(target, RecurringEventsForm.this);
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

        protected void onCreate(AjaxRequestTarget target, RecurringEventsForm form) {
            RecurrenceFormPanel.this.onCreateRecurrence(target, form);
            resetForm();
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
            eventType = (eventTypes.size()>0) ? eventTypes.get(0) : null;
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
        public void validate(Form<?> form) {
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

        public Boolean getAutoassign() {
            return autoassign;
        }

        public EventType getEventType() {
            return eventType;
        }

        public BaseOrg getOwner() {
            return owner;
        }

        public Boolean getOwnerAndDown() {
            return ownerAndDown;
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

    protected boolean isAutoAssignVisible() {
        return true;
    }

    protected boolean isOwnerDownOptionVisible() {
        return true;
    }

    protected boolean isOrgPickerVisible() {
        return true;
    }

    protected abstract List<? extends EventType> getEventTypes();

    protected abstract void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form);


}
