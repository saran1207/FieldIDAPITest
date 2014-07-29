package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.perform.PerformThingEventHelperService;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.massevent.SelectMassEventPage;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
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
    private PerformThingEventHelperService thingEventHelperService;

    private List<ThingEvent> selectedEventList;
    private IModel<EventReportCriteria> criteriaModel;
    private boolean backToSelectPage;

    ThingEvent thingEvent;

    public PerformMultiEventPage(List<ThingEvent> selectedEventList, IModel<EventReportCriteria> criteriaModel, boolean backToSelectPage) {

        this.selectedEventList = selectedEventList;
        this.criteriaModel = criteriaModel;
        this.backToSelectPage = backToSelectPage;

        try {
            thingEvent =  CopyEventFactory.copyEvent(thingEventHelperService.createEvent(selectedEventList.get(0).getId(), selectedEventList.get(0).getAsset().getId(), selectedEventList.get(0).getType().getId()));

            //Do not display any default owner/location
            thingEvent.setAdvancedLocation(null);
            thingEvent.setOwner(getCurrentUser().getOwner().getPrimaryOrg());

            event = Model.of(thingEvent);

            setEventResult(event.getObject().getEventResult());
            fileAttachments = new ArrayList<FileAttachment>();

            //doAutoSchedule();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Component createCancelLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                if(backToSelectPage) {
                    setResponsePage(new SelectMassEventPage(criteriaModel));
                } else {
                    setResponsePage(new ReportPage(criteriaModel.getObject()));
                }
            }
        };
    }

    @Override
    protected List<ThingEvent> doSave() {
        event.getObject().storeTransientCriteriaResults();
        event.getObject().setEventResult(getEventResult());

        FileDataContainer fileDataContainer = null;
        List<ThingEvent> finalList = new ArrayList<ThingEvent>();

        for(ThingEvent originalEventFromList:selectedEventList){

            ThingEvent originalEvent = thingEventHelperService.createEventFromOpenEvent(originalEventFromList.getId());

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
        if(!getAssetOwnerUpdate()){
            originalEvent.setOwner(genericEvent.getOwner());
        }
        if(!getLocationUpdate()){
            originalEvent.setAdvancedLocation(genericEvent.getAdvancedLocation());
        }
    }
}