package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.ThingEventCreationService;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
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

public abstract class ThingEventPage extends EventPage<ThingEvent> {

    @SpringBean protected ThingEventCreationService eventCreationService;
    @SpringBean private AssetService assetService;

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

    @Override
    protected boolean supportsProofTests() {
        ThingEvent event = this.event.getObject();
        return event.getThingType().getSupportedProofTests().size()>0 && event.getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ProofTestIntegration);
    }

    protected void doAutoSchedule() {
        ThingEvent e = event.getObject();

        if (null == e.getDate()) {
            return;
        }

        if(!isAutoScheduled) {
            AssetTypeSchedule schedule = e.getAsset().getType().getSchedule(e.getType(), e.getOwner());
            if (schedule != null) {
                ThingEvent eventSchedule = new ThingEvent();
                eventSchedule.setAsset(e.getAsset());
                eventSchedule.setType(e.getType());
                eventSchedule.setDueDate(schedule.getNextDate(e.getDate()));
                schedules.add(eventSchedule);
            }
            isAutoScheduled = true;
        }
    }

    protected List<EventScheduleBundle<Asset>> createEventScheduleBundles() {
        List<EventScheduleBundle<Asset>> scheduleBundles = new ArrayList<>();

        for (ThingEvent sched : schedules) {
            EventScheduleBundle bundle = new EventScheduleBundle<>(sched.getAsset(), sched.getType(), sched.getProject(), sched.getDueDate(), sched.getAssignedUserOrGroup());
            //noinspection unchecked
            scheduleBundles.add(bundle);
        }

        return scheduleBundles;
    }

    @Override
    protected ProofTestEditPanel createProofTestEditPanel(String componentId) {
        return new ProofTestEditPanel("proofTest", event.getObject().getThingType(), proofTestInfo);
    }

    @Override
    protected void retireEvent(ThingEvent event) {
        eventService.retireEvent(event);
    }

    @Override
    protected void gotoSummaryPage(ThingEvent event) {
        setResponsePage(AssetSummaryPage.class, PageParametersBuilder.uniqueId(event.getAsset().getId()));
    }

    @Override
    protected boolean targetAlreadyArchived(ThingEvent event) {
        return (assetService.findById(event.getTarget().getId()) != null);
    }

    @Override
    protected void onPreSave(ThingEvent event) {
        //if prooftest is null, then no need to "save" it
        if(proofTestInfo.getObject().getProofTestType() == null){
            return;
        }

        if(event.getProofTestInfo() == null){
            event.setProofTestInfo(new ThingEventProofTest());
        }
        event.getProofTestInfo().copyDataFrom(proofTestInfo.getObject());
    }

    @Override
    protected SchedulePicker<ThingEvent> createSchedulePicker() {
        return new SchedulePicker<ThingEvent>("schedulePicker", new PropertyModel<>(ThingEventPage.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<>(event, "asset.type")), new EventJobsForTenantModel()) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                onSchedulePickComplete(target);
            }
        };
    }

    @Override
    protected Component createTargetDetailsPanel(IModel<ThingEvent> model) {
        return new AssetDetailsPanel("targetDetailsPanel", ProxyModel.of(model, on(ThingEvent.class).getAsset()));
    }

    @Override
    protected Component createPostEventPanel(IModel<ThingEvent> event) {
        return new PostThingEventPanel("postEventPanel", event);
    }
}
