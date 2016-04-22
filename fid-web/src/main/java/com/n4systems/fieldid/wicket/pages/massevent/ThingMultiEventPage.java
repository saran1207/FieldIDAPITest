package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.event.post.PostThingEventPanel;
import com.n4systems.fieldid.wicket.pages.event.target.AssetDetailsPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.ThingEventProofTest;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public abstract class ThingMultiEventPage extends MultiEventPage<ThingEvent> {

    @SpringBean
    protected ThingEventCreationService eventCreationService;

    @SpringBean
    private AssetService assetService;

    private IModel<ThingEventProofTest> proofTestInfo;

    @Override
    protected void onInitialize() {
        proofTestInfo = new PropertyModel<>(event, "proofTestInfo");
        if (proofTestInfo.getObject() == null) {
            proofTestInfo = new Model<>(new ThingEventProofTest());
        }

        super.onInitialize();
    }

    @Override
    protected ThingEvent createNewOpenEvent() {
        ThingEvent openEvent = new ThingEvent();
        openEvent.setAsset(event.getObject().getAsset());
        return openEvent;
    }

    @SuppressWarnings("unchecked")
    protected List<EventScheduleBundle<Asset>> createEventScheduleBundles(Asset asset) {
        List<EventScheduleBundle<Asset>> scheduleBundles = new ArrayList<>();

        for (ThingEvent sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle<>(asset, sched.getType(), sched.getProject(), sched.getDueDate(), sched.getAssignedUserOrGroup());
            scheduleBundles.add(bundle);
        }

        AssetTypeSchedule schedule = asset.getType().getSchedule(event.getObject().getType(), asset.getOwner());

        if(getAutoSchedule() && schedule != null) {
            ThingEvent eventSchedule = new ThingEvent();
            eventSchedule.setAsset(asset);
            eventSchedule.setType(event.getObject().getType());
            eventSchedule.setDueDate(schedule.getNextDate(event.getObject().getDate()));

            EventScheduleBundle bundle = new EventScheduleBundle<>(asset, eventSchedule.getType(), eventSchedule.getProject(), eventSchedule.getDueDate(), eventSchedule.getAssignedUserOrGroup());
            scheduleBundles.add(bundle);
        }

        return scheduleBundles;
    }

    @Override
    protected boolean targetAlreadyArchived(ThingEvent event) {
        return (assetService.findById(event.getTarget().getId()) != null);
    }

    @Override
    protected void onPreSave(ThingEvent event) {
        //if prooftest is null, then no need to "save" it
        if(proofTestInfo.getObject() == null || proofTestInfo.getObject().getProofTestType() == null){
            event.setProofTestInfo(null);
            return;
        }

        if(event.getProofTestInfo() == null){
            event.setProofTestInfo(new ThingEventProofTest());
        }
        event.getProofTestInfo().copyDataFrom(proofTestInfo.getObject());
    }

    @Override
    protected SchedulePicker<ThingEvent> createSchedulePicker() {
        return new SchedulePicker<ThingEvent>("schedulePicker", new PropertyModel<>(ThingMultiEventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<>(event, "asset.type")), new EventJobsForTenantModel()) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                onSchedulePickComplete(target);
            }
        };
    }

    protected Component createTargetDetailsPanel(IModel<ThingEvent> model) {
        return new AssetDetailsPanel("targetDetailsPanel", ProxyModel.of(model, on(ThingEvent.class).getAsset()));
    }

    @Override
    protected Component createPostEventPanel(IModel<ThingEvent> event) {
        return new PostThingEventPanel("postEventPanel", event);
    }
}
