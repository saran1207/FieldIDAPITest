package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.RecurringAssetTypeEvent;
import com.n4systems.model.ThingEventType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetTypeRecurrenceFormPanel extends RecurrenceFormPanel<AssetType> {

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;
    @SpringBean
    private AssetTypeService assetTypeService;

    public AssetTypeRecurrenceFormPanel(String id, IModel<AssetType> model) {
        super(id, model);
    }

    @Override
    protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
        assetTypeService.addRecurringEvent((AssetType) getDefaultModelObject(), createNewEventFromForm(form));
    }

    private RecurringAssetTypeEvent createNewEventFromForm(RecurringEventsForm form) {
        RecurringAssetTypeEvent newEvent = new RecurringAssetTypeEvent((AssetType) getDefaultModelObject(), (ThingEventType)form.getEventType(), form.createRecurrence());
        newEvent.setOwner(form.getOwner());
        newEvent.setOwnerAndDown(form.getOwnerAndDown());
        newEvent.setAutoAssign(form.getAutoassign());
        newEvent.setTenant(((AssetType) getDefaultModelObject()).getTenant());
        return newEvent;
    }

    protected List<ThingEventType> getEventTypes() {
        List<ThingEventType> eventTypes = Lists.newArrayList();
        List<AssociatedEventType> associatedEventTypes = associatedEventTypesService.getAssociatedEventTypes((AssetType) getDefaultModelObject(), null);
        for (AssociatedEventType type: associatedEventTypes) {
            eventTypes.add(type.getEventType());
        }
        return eventTypes;
    }

}
