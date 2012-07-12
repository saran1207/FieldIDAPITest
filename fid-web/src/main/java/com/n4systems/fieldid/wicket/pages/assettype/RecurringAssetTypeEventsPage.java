package com.n4systems.fieldid.wicket.pages.assettype;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.EnumPropertyChoiceRenderer;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RecurringAssetTypeEventsPage extends FieldIDFrontEndPage {

    protected @SpringBean AssetTypeService assetTypeService;

    protected Long assetTypeId;
    protected IModel<AssetType> assetTypeModel;
    private ListView<RecurringAssetTypeEvent> recurringEventsList;

    public RecurringAssetTypeEventsPage(PageParameters params) {
        super(params);
        init();
    }

    private void init() {
        boolean hasEvents = getAssetType().getEventTypes().size()>0;
        add(new RecurringEventsForm("form").setVisible(hasEvents));
        add(new Label("blankSlate", new FIDLabelModel("message.no_event_types_for_asset_type")).setVisible(!hasEvents));
    }

    private RecurringAssetTypeEvent createNewEventWithDefaultValues() {
        RecurrenceType type;
        int hour;
        int minute;
        AssetType assetType = getAssetType();
        Set<EventType> eventTypes = assetType.getEventTypes();
        EventType eventType = null;
        if (eventTypes.size()>0) {
            eventType = eventTypes.iterator().next();
        }
        return new RecurringAssetTypeEvent(assetType, eventType, new Recurrence(type=RecurrenceType.MONTHLY_1ST,hour=0,minute=0));
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

        private RecurrenceTime time = RecurrenceTime.MIDNIGHT;
        private EventType eventType = null;
        private RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        private BaseOrg owner;


        public RecurringEventsForm(String id) {
            super(id);
            final AssetType assetType = getAssetType();

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());

            final List<EventType> eventTypes = Lists.newArrayList();
            for (EventType eventType:assetType.getEventTypes()) {
                eventTypes.add(eventType);
                this.eventType = eventType; // set default value for form.
            }

            final IChoiceRenderer<EventType> eventTypeRenderer = new IChoiceRenderer<EventType>() {
                @Override public Object getDisplayValue(EventType object) {
                    return new FIDLabelModel(object.getName()).getObject();
                }
                @Override public String getIdValue(EventType object, int index) {
                    return object.getId().toString();
                }
            };

            final WebMarkupContainer createContainer = new WebMarkupContainer("createContainer");
            createContainer.setOutputMarkupId(true);

            createContainer.add(new DropDownChoice<EventType>("eventType", new PropertyModel<EventType>(this, "eventType"), eventTypes, eventTypeRenderer).setNullValid(false).add(new JChosenBehavior()));
            createContainer.add(new DropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(this, "type"), recurrences, new EnumPropertyChoiceRenderer<RecurrenceType>()).setNullValid(false).add(new JChosenBehavior()));
            createContainer.add(new AutoCompleteOrgPicker("org", new PropertyModel<BaseOrg>(this, "owner")).setRequired(false));
            createContainer.add(new DropDownChoice<RecurrenceTime>("time", new PropertyModel<RecurrenceTime>(this, "time"), Arrays.asList(RecurrenceTime.values()), new EnumPropertyChoiceRenderer<RecurrenceTime>()).setNullValid(true).add(new JChosenBehavior()));

            createContainer.add(new AjaxSubmitLink("create") {
                @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    assetTypeService.addRecurringEvent(assetType, createNewEventFromForm());
                    recurringEventsList.getModel().detach();
                    target.add(RecurringEventsForm.this);
                }
                @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });
            add(createContainer);

            add(recurringEventsList = new ListView<RecurringAssetTypeEvent>("eventTypes", new RecurringEventsModel(assetTypeModel)) {
                @Override
                protected void populateItem(final ListItem<RecurringAssetTypeEvent> item) {
                    item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                    RecurringAssetTypeEvent event = (RecurringAssetTypeEvent) item.getDefaultModelObject();
                    item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                    item.add(new Label("org", new NullCoverterModel(new PropertyModel<String>(item.getDefaultModelObject(), "owner.name"), "---")));
                    item.add(new Label("time", new PropertyModel<String>(item.getDefaultModelObject(), "recurrence.displayTime")));
                    item.add(new AjaxLink("remove") {
                        @Override public void onClick(AjaxRequestTarget target) {
                            assetTypeService.deleteRecurringEvent(assetType, item.getModelObject());
                            recurringEventsList.getModel().detach();
                            target.add(RecurringEventsForm.this);
                        }
                    });
                }

            });
            recurringEventsList.setReuseItems(true);

        }

        private RecurringAssetTypeEvent createNewEventFromForm() {
            Recurrence recurrence = new Recurrence(type,time.getHour(),time.getMinutes());
            RecurringAssetTypeEvent clone = new RecurringAssetTypeEvent(getAssetType(), eventType, recurrence);
            clone.setOwner(owner);
            return clone;
        }

    }


    private class RecurringEventsModel extends LoadableDetachableModel<List<RecurringAssetTypeEvent>> {

        private IModel<AssetType> model;

        public RecurringEventsModel(IModel<AssetType> assetTypeModel) {
            this.model = assetTypeModel;
        }

        @Override
        protected List<RecurringAssetTypeEvent> load() {
            return Lists.newArrayList(assetTypeModel.getObject().getRecurringAssetTypeEvents().iterator());
        }

        @Override
        public void detach() {
            super.detach();
            assetTypeModel.detach();
        }
    }


}
