package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.model.EventType;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OrgRecurringEventsFormPanel extends RecurrenceFormPanel {

    private @SpringBean PlaceService placeService;

    private final IModel<? extends BaseOrg> model;

    public OrgRecurringEventsFormPanel(String id, IModel<? extends BaseOrg> model) {
        super(id, model);
        this.model = model;
    }

    @Override
    protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
        placeService.addRecurringEvent();
    }

    protected List<EventType> getEventTypes() {
        //return model.getObject().getPlaces();
        return Lists.newArrayList(EventTypeBuilder.anEventType().named("Visual").build());
    }


    @Override
    protected boolean isAutoAssignVisible() {
        return false;
    }

    @Override
    protected boolean isOrgPickerVisible() {
        return false;
    }

    @Override
    protected boolean isOwnerDownOptionVisible() {
        return false;
    }
}
