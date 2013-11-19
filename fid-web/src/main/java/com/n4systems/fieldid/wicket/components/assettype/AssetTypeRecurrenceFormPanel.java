package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventType;
import com.n4systems.model.RecurringAssetTypeEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetTypeRecurrenceFormPanel extends RecurrenceFormPanel {

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;
    @SpringBean
    private AssetTypeService assetTypeService;

    private IModel<AssetType> assetType;

    public AssetTypeRecurrenceFormPanel(String id, IModel<AssetType> model) {
        super(id, model);
        this.assetType = model;
    }

    @Override
    protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
        assetTypeService.addRecurringEvent(assetType.getObject(), createNewEventFromForm(form));
    }

    private RecurringAssetTypeEvent createNewEventFromForm(RecurringEventsForm form) {
        RecurringAssetTypeEvent newEvent = new RecurringAssetTypeEvent(assetType.getObject(), form.getEventType(), form.createRecurrence());
        newEvent.setOwner(form.getOwner());
        newEvent.setOwnerAndDown(form.getOwnerAndDown());
        newEvent.setAutoAssign(form.getAutoassign());
        newEvent.setTenant(assetType.getObject().getTenant());
        return newEvent;
    }

    protected List<EventType> getEventTypes() {
        List<EventType> eventTypes = Lists.newArrayList();
        List<AssociatedEventType> associatedEventTypes = associatedEventTypesService.getAssociatedEventTypes(assetType.getObject(), null);
        for (AssociatedEventType type: associatedEventTypes) {
            eventTypes.add(type.getEventType());
        }
        return eventTypes;
    }

}
