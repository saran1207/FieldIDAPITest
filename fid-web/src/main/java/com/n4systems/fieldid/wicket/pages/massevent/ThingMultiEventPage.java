package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.pages.event.post.PostThingEventPanel;
import com.n4systems.fieldid.wicket.pages.event.target.AssetDetailsPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
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

    @SpringBean protected ThingEventCreationService eventCreationService;

    protected IModel<ThingEventProofTest> proofTestInfo;

    @Override
    protected void onInitialize() {
        proofTestInfo = new PropertyModel<ThingEventProofTest>(event, "proofTestInfo");
        if (proofTestInfo.getObject() == null) {
            proofTestInfo = new Model<ThingEventProofTest>(new ThingEventProofTest());
        }

        super.onInitialize();
    }

    @Override
    protected ThingEvent createNewOpenEvent() {
        ThingEvent openEvent = new ThingEvent();
        openEvent.setAsset(event.getObject().getAsset());
        return openEvent;
    }

    protected List<EventScheduleBundle<Asset>> createEventScheduleBundles(Asset asset) {
        List<EventScheduleBundle<Asset>> scheduleBundles = new ArrayList<EventScheduleBundle<Asset>>();

        for (ThingEvent sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle<Asset>(asset, sched.getType(), sched.getProject(), sched.getDueDate(), sched.getAssignedUserOrGroup());
            scheduleBundles.add(bundle);
        }

        AssetTypeSchedule schedule = asset.getType().getSchedule(event.getObject().getType(), asset.getOwner());

        if(getAutoSchedule() && schedule != null) {
            ThingEvent eventSchedule = new ThingEvent();
            eventSchedule.setAsset(asset);
            eventSchedule.setType(event.getObject().getType());
            eventSchedule.setDueDate(schedule.getNextDate(event.getObject().getDate()));

            EventScheduleBundle bundle = new EventScheduleBundle<Asset>(asset, eventSchedule.getType(), eventSchedule.getProject(), eventSchedule.getDueDate(), eventSchedule.getAssignedUserOrGroup());
            scheduleBundles.add(bundle);
        }

        return scheduleBundles;
    }

    @Override
    protected boolean targetAlreadyArchived(ThingEvent event) {
        return persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getAsset().getId()).isArchived();
    }

    @Override
    protected void onPreSave(ThingEvent event) {
        //if prooftest is null, then no need to "save" it
        if(proofTestInfo.getObject().getProofTestType() == null){
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
        return new SchedulePicker<ThingEvent>("schedulePicker", new PropertyModel<ThingEvent>(ThingMultiEventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(event, "asset.type")), new EventJobsForTenantModel()) {
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
