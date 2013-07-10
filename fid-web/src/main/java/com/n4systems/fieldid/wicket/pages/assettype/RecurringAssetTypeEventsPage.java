package com.n4systems.fieldid.wicket.pages.assettype;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.util.EnumPropertyChoiceRenderer;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.*;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.date.DateService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.validation.FormValidatorAdapter;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.*;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class RecurringAssetTypeEventsPage extends FieldIDFrontEndPage {

    private @SpringBean AssetTypeService assetTypeService;
    private @SpringBean AsyncService asyncService;
    private @SpringBean DateService dateService;

    protected Long assetTypeId;
    protected IModel<AssetType> assetTypeModel;
    private RefreshingView<RecurringAssetTypeEvent> recurringEventsList;

    public RecurringAssetTypeEventsPage(PageParameters params) {
        super(params);
        init();
    }

    private void init() {
        boolean hasEvents = getAssetType().getEventTypes().size()>0;
        add(new RecurringEventsForm("form").setVisible(hasEvents));
        add(new Label("blankSlate", new FIDLabelModel("message.no_event_types_for_asset_type")).setVisible(!hasEvents));
    }

    private AssetType getAssetType() {
        return assetTypeModel.getObject();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.recurring_events"));
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        Preconditions.checkArgument(params.get("uniqueId") != null, "must pass asset type Id to page via uniqueId parameter.");
        assetTypeId = params.get("uniqueID").toLong();
        assetTypeModel = new EntityModel<AssetType>(AssetType.class, assetTypeId);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("assetTypes.action").build(),
                aNavItem().label("nav.view").page("assetType.action").params(uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.edit").page("assetTypeEdit.action").params(uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.event_type_associations").page("selectEventTypes.action").params(param("assetTypeId", assetTypeId)).build(),
                aNavItem().label("nav.event_frequencies").page("eventFrequencies.action").params(param("assetTypeId", assetTypeId)).build(),
                aNavItem().label("label.recurring_events").page(RecurringAssetTypeEventsPage.class).params(uniqueId(assetTypeId)).build(),
                aNavItem().label("l" +
                        "" +
                        "abel.subassets").page("assetTypeConfiguration.action").params(uniqueId(assetTypeId)).build(),
                aNavItem().label("nav.add").page("assetTypeEdit.action").onRight().build()
        ));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/recurring.css");
        response.renderCSSReference("style/site_wide.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }



    private class RecurringEventsForm extends Form implements IFormValidator {

        // private fields used to back form components.
        private RecurrenceTimeOfDay time = RecurrenceTimeOfDay.NINE_AM;
        private List<RecurrenceTimeOfDay> times = Lists.newArrayList(RecurrenceTimeOfDay.NINE_AM);
        private EventType eventType = null;
        private RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        private BaseOrg owner;
        private PredefinedLocation location;
        private Date dateTime = dateService.nowInUsersTimeZone().toDate();
        private TimeContainer timePicker;
        private DateTimePicker dateTimepicker;
        private DropDownChoice<RecurrenceType> recurrenceTypeDropDown;
        private final FIDFeedbackPanel feedback;
        private Object x;

        public RecurringEventsForm(String id) {
            super(id);

            add(feedback = new FIDFeedbackPanel("feedbackPanel"));

            final AssetType assetType = getAssetType();

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());

             final List<EventType> eventTypes = Lists.newArrayList(assetType.getEventTypes());
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

            //??withAutoUpdate();
            inputContainer.add(new AutoCompleteOrgPicker("org", new PropertyModel<BaseOrg>(this, "owner")).setRequired(false));

            recurrenceTypeDropDown.setNullValid(false).setOutputMarkupId(true).add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    updateTimeComponents(recurrenceTypeDropDown.getModel().getObject());
                    target.add(timePicker, dateTimepicker);
                }
            });

            inputContainer.add(new AjaxSubmitLink("create") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    addRecurringEvent(assetType, createNewEventFromForm());
                    target.add(RecurringEventsForm.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            }.add(new AjaxIndicatorAppender()));
            add(inputContainer);

            recurringEventsList = new RefreshingView<RecurringAssetTypeEvent>("eventTypes") {
                @Override protected Iterator<IModel<RecurringAssetTypeEvent>> getItemModels() {
                    return new RecurringEventsModel(assetTypeModel).getItems().iterator();
                }

                @Override protected void populateItem(final Item<RecurringAssetTypeEvent> item) {
                    item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                    RecurringAssetTypeEvent event = (RecurringAssetTypeEvent) item.getDefaultModelObject();
                    item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                    item.add(new Label("org", new NullCoverterModel(new PropertyModel<String>(item.getDefaultModelObject(), "owner.name"), "---")));
                    item.add(new Label("time", new DisplayTimeModel(new PropertyModel<Set<RecurrenceTime>>(item.getDefaultModelObject(), "recurrence.times"))));
                    item.add(new IndicatingAjaxLink("remove") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            deleteRecurringEvent(assetType, item.getModelObject());
                            target.add(RecurringEventsForm.this);
                        }
                    });
                }

                @Override protected Item<RecurringAssetTypeEvent> newItem(String id, int index, IModel<RecurringAssetTypeEvent> recurringAssetTypeEventIModel) {
                    return super.newItem(id, index, recurringAssetTypeEventIModel);
                }

            };

            WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer") {
                @Override public boolean isVisible() {
                    return new RecurringEventsModel(assetTypeModel).getItems().size() > 0;
                }
            };
            tableContainer.add(recurringEventsList);
            add(tableContainer);

            add(new FormValidatorAdapter(RecurringEventsForm.this));

            updateTimeComponents(type);
        }

        private RecurringAssetTypeEvent createNewEventFromForm() {
            RecurringAssetTypeEvent newEvent = new RecurringAssetTypeEvent(getAssetType(), eventType, createRecurrence());
            newEvent.setOwner(owner);
            newEvent.setTenant(assetTypeModel.getObject().getTenant());
            return newEvent;
        }

        private Recurrence createRecurrence() {
            if ( type.requiresDate() ) {
                return new Recurrence(type).withDayAndTime(dateTime);
            } else if (type.canHaveMultipleTimes()) {
                return new Recurrence(type).withTimes(times);
            } else {
                return new Recurrence(type).withTime(time);
            }
        }

        private TimeContainer createTimePicker() {
            return new TimeContainer("timeContainer");
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


        class TimeContainer extends WebMarkupContainer {

            private FidDropDownChoice singleTime;
            private MultiSelectDropDownChoice<RecurrenceTimeOfDay> multipleTime;

            public TimeContainer(String id) {
                super(id);
                setOutputMarkupId(true);
                setOutputMarkupPlaceholderTag(true);

                singleTime = new FidDropDownChoice<RecurrenceTimeOfDay>("time", new PropertyModel<RecurrenceTimeOfDay>(RecurringEventsForm.this, "time"), Arrays.asList(RecurrenceTimeOfDay.values()), new EnumPropertyChoiceRenderer<RecurrenceTimeOfDay>());
                add(singleTime.setNullValid(true).setOutputMarkupId(true));
                multipleTime = new MultiSelectDropDownChoice<RecurrenceTimeOfDay>("multipleTimes", new PropertyModel<List<RecurrenceTimeOfDay>>(RecurringEventsForm.this, "times"), Arrays.asList(RecurrenceTimeOfDay.values()), new EnumPropertyChoiceRenderer<RecurrenceTimeOfDay>());
                add(multipleTime.setOutputMarkupId(true));

                singleTime.add(new UpdateComponentOnChange());
                multipleTime.add(new UpdateComponentOnChange());
            }

            public void updateComponents(RecurrenceType recurrenceType) {
                toggle(recurrenceType.canHaveMultipleTimes());
            }

            private void toggle(boolean b) {
                multipleTime.setVisible(b);
                singleTime.setVisible(!b);
            }

            public MultiSelectDropDownChoice<RecurrenceTimeOfDay> getMultipleTime() {
                return multipleTime;
            }

            public FidDropDownChoice getSingleTime() {
                return singleTime;
            }
        }

    }


    private void addRecurringEvent(final AssetType assetType, final RecurringAssetTypeEvent event) {
        assetTypeService.addRecurringEvent(assetType, event);
    }

    private void deleteRecurringEvent(final AssetType assetType, final RecurringAssetTypeEvent event) {
        assetTypeService.purgeRecurringEvent(event);
    }


    private class RecurringEventsModel extends LoadableDetachableModel<List<RecurringAssetTypeEvent>> {

        private IModel<AssetType> model;

        public RecurringEventsModel(IModel<AssetType> assetTypeModel) {
            this.model = assetTypeModel;
        }

        @Override
        protected List<RecurringAssetTypeEvent> load() {
            return assetTypeService.getRecurringEvents(model.getObject());
        }

        @Override
        public void detach() {
            super.detach();
            assetTypeModel.detach();
        }

        public List<IModel<RecurringAssetTypeEvent>> getItems() {
            List<IModel<RecurringAssetTypeEvent>> result = Lists.newArrayList();
            for (RecurringAssetTypeEvent recurringEvent:load()) {
                result.add(Model.of(recurringEvent));
            }
            return result;
        }

    }


    class DisplayTimeModel extends Model<String> {

        private PropertyModel<Set<RecurrenceTime>> model;

        DisplayTimeModel(PropertyModel<Set<RecurrenceTime>> model) {
            this.model = model;
        }

        @Override
        public String getObject() {
            List<String> result = Lists.newArrayList();
            Iterator<RecurrenceTime> iterator = model.getObject().iterator();
            while (iterator.hasNext()) {
                result.add(convertTimeToString(iterator.next()));
            }
            return Joiner.on(",").join(result);
        }

        protected String convertTimeToString(RecurrenceTime time) {
            String monthDay = (time.hasDay()) ?
                    new LocalDate().withMonthOfYear(time.getMonth()).withDayOfMonth(time.getDayOfMonth()).toString("MMM d") + " " :
                    "";

            LocalTime localTime = new LocalTime().withHourOfDay(time.getHour()).withMinuteOfHour(time.getMinute());

            String clock = localTime.toString("hh:mm a");

            return monthDay + clock;
        }

    }


}
