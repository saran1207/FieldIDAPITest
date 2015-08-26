package com.n4systems.fieldid.wicket.pages.masterevent;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.util.NewEventTransientCriteriaResultPopulator;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.EventType;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PerformSubEventPage extends SubEventPage {

    public PerformSubEventPage(IModel<ThingEvent> masterEvent, Long subAssetId, Long subEventTypeID) {
        this.masterEvent = masterEvent;
        Asset subAsset = assetService.getAsset(subAssetId, Asset.POST_FETCH_ALL_PATHS);
        EventType subEventType = eventTypeService.getEventType(subEventTypeID);
        fileAttachments = Lists.newArrayList();
        this.event = Model.of(createSubEvent(subAsset, subEventType));
    }

    protected SubEvent createSubEvent(Asset subAsset, EventType subEventType) {
        SubEvent subEvent = new SubEvent();
        subEvent.setAsset(subAsset);
        subEvent.setType(subEventType);
        subEvent.setEventForm(subEventType.getEventForm());
        subEvent.setTenant(subAsset.getTenant());
        new NewEventTransientCriteriaResultPopulator().populateTransientCriteriaResultsForEvent(subEvent);

        return subEvent;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.perform_subevent"));
    }

    @Override
    protected void onSave() {
        masterEvent.getObject().getSubEvents().add(event.getObject());
        setResponsePage(new PerformMasterEventPage(masterEvent));
    }
}
