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
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
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
    private List<RecurringAssetTypeEvent> recurringEvents;

    public RecurringAssetTypeEventsPage(PageParameters params) {
        super(params);
        init();
    }

    private void init() {
        final AssetType assetType = assetTypeModel.getObject();
        recurringEvents = assetType.getRecurringAssetTypeEvents();
        newEvent = new RecurringAssetTypeEvent(assetType);
        add(new RecurringEventsForm("form", assetType));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.recurring_events"));
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        Preconditions.checkArgument(params.get("uniqueId")!=null, "must pass asset type Id to page via uniqueId parameter.");
        assetTypeId = params.get("uniqueID").toLong();
        assetTypeModel = new EntityModel<AssetType>(AssetType.class, assetTypeId);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/recurring.css");
        response.renderCSSReference("style/site_wide.css");
    }

    public Long getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Long assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public IModel<AssetType> getAssetTypeModel() {
        return assetTypeModel;
    }


    private class RecurringEventsForm extends Form {

        public RecurringEventsForm(String id, final AssetType assetType) {
            super(id, new CompoundPropertyModel(newEvent));

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
            createContainer.add(new TextField<Integer>("time", new PropertyModel<Integer>(newEvent, "recurrence.hour")));

            AjaxSubmitLink create;
            createContainer.add(create = new AjaxSubmitLink("create") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    recurringEvents.add(cloneNewEvent(newEvent));
                    recurringEventsList.getModel().detach();
                    target.add(RecurringEventsForm.this);
                    target.add(createContainer);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });
            add(createContainer);

            add(recurringEventsList = new ListView<RecurringAssetTypeEvent>("eventTypes", new PropertyModel<List<RecurringAssetTypeEvent>>(RecurringAssetTypeEventsPage.this, "recurringEvents")){
                @Override
                protected void populateItem(ListItem<RecurringAssetTypeEvent> item) {
                    item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                    item.add(new Label("recurrence", new PropertyModel<String>(item.getDefaultModelObject(), "recurrence.type.name")));
                    item.add(new Label("org", new PropertyModel<String>(item.getDefaultModelObject(), "owner.displayName")));
                    item.add(new Label("time", new PropertyModel<String>(item.getDefaultModelObject(), "recurrence.hour")));
                }

            });
            recurringEventsList.setReuseItems(true);

        }

    }

    private RecurringAssetTypeEvent cloneNewEvent(RecurringAssetTypeEvent event) {
        RecurringAssetTypeEvent result = new RecurringAssetTypeEvent();
        result.setAssetType(event.getAssetType());
        result.setEventType(event.getEventType());
        result.setOwner(event.getOwner());
        Recurrence recurrence = new Recurrence(event.getRecurrence().getType(), event.getRecurrence().getHour());
        result.setRecurrence(recurrence);
        return result;
    }

}
