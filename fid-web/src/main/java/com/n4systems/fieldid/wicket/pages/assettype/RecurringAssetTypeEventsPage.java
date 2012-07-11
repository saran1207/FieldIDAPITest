package com.n4systems.fieldid.wicket.pages.assettype;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

public class RecurringAssetTypeEventsPage extends FieldIDFrontEndPage {

    protected @SpringBean AssetTypeService assetTypeService;

    protected Long assetTypeId;
    protected IModel<AssetType> assetTypeModel;
    private ListView<RecurringAssetTypeEvent> recurringEventsList;
    private RecurringAssetTypeEvent newEvent;

    public RecurringAssetTypeEventsPage(PageParameters params) {
        super(params);
        init();
    }

    private void init() {
        newEvent = createNewEventWithDefaultValues();
        add(new RecurringEventsForm("form"));
    }

    private RecurringAssetTypeEvent createNewEventWithDefaultValues() {
        AssetType assetType = getAssetType();
        EventType eventType = assetType.getEventTypes().iterator().next();
        RecurrenceType type = RecurrenceType.MONTHLY_1ST;
        int hour = 0; // default to midnight
        return new RecurringAssetTypeEvent(assetType, eventType, new Recurrence(type,hour));
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

    private RecurringAssetTypeEvent copyOf(RecurringAssetTypeEvent event) {
        Recurrence recurrence = new Recurrence(event.getRecurrence().getType(), event.getRecurrence().getHour());
        RecurringAssetTypeEvent clone = new RecurringAssetTypeEvent(event.getAssetType(), event.getEventType(), recurrence);
        clone.setOwner(event.getOwner());
        return clone;
    }



    private class RecurringEventsForm extends Form {

        public RecurringEventsForm(String id) {
            super(id, new CompoundPropertyModel(newEvent));
            final AssetType assetType = getAssetType();

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());
            final IChoiceRenderer<RecurrenceType> renderer = new IChoiceRenderer<RecurrenceType>() {
                @Override public Object getDisplayValue(RecurrenceType object) {
                    return new FIDLabelModel("enum."+object.toString()).getObject();
                }
                @Override public String getIdValue(RecurrenceType object, int index) {
                    return object.name();
                }
            };

            final List<EventType> eventTypes = Lists.newArrayList();
            for (EventType eventType:assetType.getEventTypes()) {
                eventTypes.add(eventType);
            }
            newEvent.setEventType(eventTypes.get(0));

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

            createContainer.add(new DropDownChoice<EventType>("eventType", new PropertyModel<EventType>(newEvent, "eventType"), eventTypes, eventTypeRenderer).setNullValid(false));
            createContainer.add(new DropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(newEvent, "recurrence.type"), recurrences, renderer).setNullValid(false));
            createContainer.add(new AutoCompleteOrgPicker("org", new PropertyModel<BaseOrg>(newEvent, "owner")).setRequired(false));
            createContainer.add(new TextField<Integer>("time", new PropertyModel<Integer>(newEvent, "recurrence.hour")).setRequired(false));

            AjaxSubmitLink create;
            createContainer.add(create = new AjaxSubmitLink("create") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    assetTypeService.addRecurringEvent(assetType, copyOf(newEvent));
                    //TODO DD : ?? do i need this??
                    recurringEventsList.getModel().detach();
                    target.add(RecurringEventsForm.this);
                    target.add(createContainer);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {

                }
            });
            add(createContainer);

            add(recurringEventsList = new ListView<RecurringAssetTypeEvent>("eventTypes", new RecurringEventsModel(assetTypeModel)) {
                @Override
                protected void populateItem(final ListItem<RecurringAssetTypeEvent> item) {
                    item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                    RecurringAssetTypeEvent event = (RecurringAssetTypeEvent) item.getDefaultModelObject();
                    item.add(new Label("recurrence", new FIDLabelModel("enum."+event.getRecurrence().getType())));
                    item.add(new Label("org", new NullCoverterModel(new PropertyModel<String>(item.getDefaultModelObject(), "owner.displayName"), "---")));
                    item.add(new Label("time", new PropertyModel<String>(item.getDefaultModelObject(), "recurrence.hour")));
                    item.add(new AjaxLink("remove") {
                        @Override public void onClick(AjaxRequestTarget target) {
                            assetTypeService.deleteRecurringEvent(assetType, item.getModelObject());
                            System.out.println("removing..." + item.getModelObject().getRecurrence());
                            recurringEventsList.getModel().detach();
                            target.add(RecurringEventsForm.this);
                            target.add(createContainer);
                        }
                    });
                }

            });
            recurringEventsList.setReuseItems(true);

        }

    }


    private class RecurringEventsModel extends LoadableDetachableModel<List<RecurringAssetTypeEvent>> {

        private IModel<AssetType> model;

        public RecurringEventsModel(IModel<AssetType> assetTypeModel) {
            this.model = assetTypeModel;
        }

        @Override
        protected List<RecurringAssetTypeEvent> load() {
            return assetTypeModel.getObject().getRecurringAssetTypeEvents();
        }

        @Override
        public void detach() {
            super.detach();
            assetTypeModel.detach();
        }
    }


    private class NullCoverterModel extends Model<String> {

        private String nullReplacement;
        private IModel<String> model;

        public NullCoverterModel(IModel<String> model, String nullReplacement) {
            this.model = model;
            this.nullReplacement = nullReplacement;
        }

        @Override
        public String getObject() {
            String value = model.getObject();
            return (StringUtils.isEmpty(value)) ? nullReplacement : value;
        }
    }

}
