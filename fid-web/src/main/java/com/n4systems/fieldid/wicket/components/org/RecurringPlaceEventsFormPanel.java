package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class RecurringPlaceEventsFormPanel extends RecurrenceFormPanel<BaseOrg> {

    @SpringBean
    private RecurringScheduleService recurringScheduleService;

    private final IModel<BaseOrg> model;

    public RecurringPlaceEventsFormPanel(String id, IModel<BaseOrg> model) {
        super(id, model);
        this.model = model;
    }

    @Override
    protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
        recurringScheduleService.addRecurringEvent(((BaseOrg) getDefaultModelObject()), createRecurringEventFromForm(form));
    }

    private RecurringPlaceEvent createRecurringEventFromForm(RecurringEventsForm form) {
        RecurringPlaceEvent newEvent = new RecurringPlaceEvent(((BaseOrg)getDefaultModelObject()), (PlaceEventType)form.getEventType(), form.createRecurrence());
        newEvent.setOwner(model.getObject());
        newEvent.setAutoAssign(form.getAutoassign());
        newEvent.setTenant(model.getObject().getTenant());
        return newEvent;
    }

    protected List<PlaceEventType> getEventTypes() {
        return Lists.newArrayList(((BaseOrg)getDefaultModelObject()).getEventTypes());
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
