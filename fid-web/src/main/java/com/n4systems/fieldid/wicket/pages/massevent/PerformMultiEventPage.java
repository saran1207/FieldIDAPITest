package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.perform.PerformThingEventHelperService;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2014-07-22.
 */
public class PerformMultiEventPage extends ThingMultiEventPage {
    @SpringBean
    private EventService eventService;
    @SpringBean
    private PersistenceService persistenceService;
    @SpringBean
    private EventScheduleService eventScheduleService;
    @SpringBean
    private AssetService assetService;
    @SpringBean
    protected AssignmentEscalationRuleService ruleService;

    @SpringBean
    private PerformThingEventHelperService thingEventHelperService;

    private List<ThingEvent> selectedEventList;

    ThingEvent thingEvent;

    public PerformMultiEventPage(List<ThingEvent> selectedEventList, MassEventOrigin massEventOrigin) {
        this.selectedEventList = selectedEventList;
        setMassEventOrigin(massEventOrigin);

        try {
            thingEvent =  CopyEventFactory.copyEvent(thingEventHelperService.createEvent(selectedEventList.get(0).getId(), selectedEventList.get(0).getAsset().getId(), selectedEventList.get(0).getType().getId()));

            //This generic event should not have any id's associated to it when copying it over to the original events.
            thingEvent.setId(null);
            thingEvent.setMobileGUID(null);
            //Do not display any default owner/location
            thingEvent.setAdvancedLocation(null);
            thingEvent.setOwner(getCurrentUser().getOwner().getPrimaryOrg());
            thingEvent.setPrintable(thingEvent.getType().isPrintable());
            thingEvent.setInitialResultBasedOnScoreOrOneClicksBeingAvailable();

            event = Model.of(thingEvent);

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Component createCancelLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                onCancel();
            }
        };
    }

    public void onCancel() {}

    @Override
    protected List<ThingEvent> doSave() {
        event.getObject().storeTransientCriteriaResults();
        event.getObject().setEventResult(getEventResult());
        event.getObject().setWorkflowState(WorkflowState.CLOSED);

        FileDataContainer fileDataContainer = null;
        List<ThingEvent> finalList = new ArrayList<ThingEvent>();

        for(ThingEvent originalEventFromList:selectedEventList){

            ThingEvent originalEvent;
            if(originalEventFromList.isNew())
                originalEvent = originalEventFromList;
            else {
                originalEvent = thingEventHelperService.createEventFromOpenEvent(originalEventFromList.getId());
            }

            originalEvent.setProofTestInfo(event.getObject().getProofTestInfo());

            //save the event
            //persistenceService.save(originalEvent);

            if(event.getObject().getPerformedBy() == null) {
                event.getObject().setPerformedBy(getCurrentUser());
            }

            if(originalEvent.getSectionResults() != null) {
                originalEvent.storeTransientCriteriaResults();
            }
            copyMassEventInfo(originalEvent);

            ThingEvent savedEvent = eventCreationService.createEventWithSchedules(originalEvent, 0L, fileDataContainer, fileAttachments, createEventScheduleBundles(originalEvent.getAsset()));

            finalList.add(savedEvent);
        }
        return finalList;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.preform_mass_event"));
    }

    protected void copyMassEventInfo(ThingEvent originalEvent){

        ThingEvent genericEvent = event.getObject();

        CopyEventFactory.copyEventForMassEvents(originalEvent, genericEvent);

        for (CriteriaResult result : originalEvent.getResults()) {
            for (Event action : result.getActions()) {
                action.setTenant(originalEvent.getTenant());
            }
        }

        originalEvent.setTriggersIntoResultingActions(originalEvent);

        originalEvent.setPrintable(event.getObject().isPrintable());

        if(getAssetOwnerUpdate()) {
            originalEvent.setOwner(originalEvent.getAsset().getOwner());
        } else {
            originalEvent.setOwner(genericEvent.getOwner());
        }
        if(getLocationUpdate()) {
            originalEvent.setAdvancedLocation(originalEvent.getAsset().getAdvancedLocation());
        } else {
            originalEvent.setAdvancedLocation(genericEvent.getAdvancedLocation());
        }
    }
}