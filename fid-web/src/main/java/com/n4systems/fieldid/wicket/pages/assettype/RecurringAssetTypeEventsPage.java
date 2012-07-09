package com.n4systems.fieldid.wicket.pages.assettype;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.*;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecurringAssetTypeEventsPage extends FieldIDFrontEndPage {

    protected @SpringBean AssetTypeService assetTypeService;

//    private @SpringBean PersistenceService persistenceService;

    protected Long assetTypeId;
    protected IModel<AssetType> assetTypeModel;
    private ListView<RecurringAssetTypeEvent> recurringEventsList;


    public RecurringAssetTypeEventsPage(PageParameters params) {
        super(params);
        final AssetType assetType = assetTypeModel.getObject();

        add(new Label("label", new PropertyModel<String>(assetTypeModel, "name")));

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


    private List<RecurringAssetTypeEvent> getRecurringEvents(AssetType assetType) {
        Map<EventType, RecurringAssetTypeEvent> map = Maps.newHashMap();
        for (EventType eventType:assetType.getAllEventTypes()) {  // fill with defaults first...
            map.put(eventType, new RecurringAssetTypeEvent(assetType, eventType, new Recurrence()));
        }
        for (RecurringAssetTypeEvent recurring:assetType.getRecurringAssetTypeEvents()) {
            map.put(recurring.getEventType(), recurring);
        }
        Preconditions.checkState(map.size() == assetType.getEventTypes().size());
        return new ArrayList<RecurringAssetTypeEvent>(map.values());
    }


    private class RecurringEventsForm extends Form {

        public RecurringEventsForm(String id, final AssetType assetType) {
            super(id);

            final List<RecurrenceType> recurrences= Arrays.asList(RecurrenceType.values());
            final IChoiceRenderer renderer = new IChoiceRenderer<RecurrenceType>() {
                @Override public Object getDisplayValue(RecurrenceType object) {
                    return new FIDLabelModel(object.toString()).getObject();
                }
                @Override public String getIdValue(RecurrenceType object, int index) {
                    return object.name();
                }
            };

            add(recurringEventsList = new ListView<RecurringAssetTypeEvent>("eventTypes", new RecurrencesModel(assetTypeModel)) {
                @Override
                protected void populateItem(ListItem<RecurringAssetTypeEvent> item) {
                    item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                    item.add(new DropDownChoice<RecurrenceType>("recurrence", new PropertyModel<RecurrenceType>(item.getDefaultModelObject(), "recurrence.type"), recurrences, renderer));
                }
            });

            add(new SubmitLink("save"));
        }

        @Override
        protected void onSubmit() {
            AssetType assetType = assetTypeModel.getObject();
            assetTypeService.udpateRecurringEvents(assetType, recurringEventsList.getModel().getObject());
            recurringEventsList.getModel().detach();
            assetTypeModel.detach();
            setResponsePage(new DashboardPage());
        }

    }

    private class RecurrencesModel implements IModel<List<RecurringAssetTypeEvent>> {

        private List<RecurringAssetTypeEvent> recurrences;

        public RecurrencesModel(IModel<AssetType> assetTypeModel) {

        }
        @Override
        public List<RecurringAssetTypeEvent> getObject() {
            return recurrences == null ? recurrences = new ArrayList<RecurringAssetTypeEvent>(getRecurringEvents(assetTypeModel.getObject())) : recurrences;
        }

        @Override
        public void setObject(List<RecurringAssetTypeEvent> object) {
            recurrences = object;
        }

        @Override
        public void detach() {
            //recurrences = null;
        }
    }


}
