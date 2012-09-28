package com.n4systems.fieldid.wicket.pages.assettype;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
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
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class RecurringAssetTypeEventsPage extends FieldIDFrontEndPage {

    private @SpringBean AssetTypeService assetTypeService;
    private @SpringBean AsyncService asyncService;

    protected Long assetTypeId;
    protected IModel<AssetType> assetTypeModel;
    private RefreshingView<RecurringAssetTypeEvent> recurringEventsList;
    private static final String SHOW_MESSAGE_JS  = "$('.fade-out').show();setTimeout( function(){ $('.fade-out').fadeOut(2000);}, 3000);";

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
                aNavItem().label("label.subassets").page("assetTypeConfiguration.action").params(uniqueId(assetTypeId)).build(),
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



    private class RecurringEventsForm extends Form {

        // private fields used to back form components.
        private RecurrenceTime time = RecurrenceTime.MIDNIGHT;
        private EventType eventType = null;
        private RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        private BaseOrg owner;
        private PredefinedLocation location;
        private Date dateTime = LocalDate.now().toDate();
        Component timePicker;
        Component dateTimepicker;

        public RecurringEventsForm(String id) {
            super(id);
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

            dateTimepicker = new DateTimePicker("dateTime", new PropertyModel<Date>(this, "dateTime"),true).withMonthsDisplayed(1).withNoAllDayCheckbox().setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
            timePicker = createTimePicker();

            inputContainer.add(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(this, "eventType"), eventTypes, eventTypeRenderer).setNullValid(false));
            final DropDownChoice<RecurrenceType> recurrenceTypeDropDown = new FidDropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(this, "type"), recurrences, new EnumPropertyChoiceRenderer<RecurrenceType>());
            inputContainer.add(recurrenceTypeDropDown);
            inputContainer.add(dateTimepicker);
            inputContainer.add(timePicker);
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
                }
            });
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
                    item.add(new Label("time", new PropertyModel<String>(item.getDefaultModelObject(), "recurrence.displayTime")));
                    item.add(new AjaxLink("remove") {
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

            updateTimeComponents(type);
        }

        private RecurringAssetTypeEvent createNewEventFromForm() {
            Recurrence recurrence = null;
            if ( type.requiresDate() ) {
                DateTime date = new DateTime(dateTime);
                recurrence = new Recurrence(type,date.getHourOfDay(), date.getMinuteOfHour(), new LocalDate(dateTime).toDate());
            } else {
                recurrence = new Recurrence(type,time.getHour(),time.getMinutes());
            }
            RecurringAssetTypeEvent newEvent = new RecurringAssetTypeEvent(getAssetType(), eventType, recurrence);
            newEvent.setOwner(owner);
            newEvent.setTenant(assetTypeModel.getObject().getTenant());
            return newEvent;
        }

        private Component createTimePicker() {
            WebMarkupContainer container = (WebMarkupContainer) new WebMarkupContainer("timeContainer").setOutputMarkupId(true);
            Component dropDown = new FidDropDownChoice<RecurrenceTime>("time", new PropertyModel<RecurrenceTime>(this, "time"), Arrays.asList(RecurrenceTime.values()), new EnumPropertyChoiceRenderer<RecurrenceTime>()).setNullValid(true).setOutputMarkupId(true);
            container.add(dropDown);
            container.setOutputMarkupPlaceholderTag(true);
            return container;
        }

        private void updateTimeComponents(RecurrenceType recurrenceType) {
            dateTimepicker.setVisible(recurrenceType.requiresDate());
            timePicker.setVisible(!recurrenceType.requiresDate());
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


}
