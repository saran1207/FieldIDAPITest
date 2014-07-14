package com.n4systems.fieldid.service.event;

import com.amazonaws.AmazonClientException;
import com.n4systems.ejb.impl.EventResultCalculator;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ContentTypeUtil;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ThingEventCreationService extends EventCreationService<ThingEvent, Asset> {

    @Override
    protected ThingEvent createEvent() {
        return new ThingEvent();
    }

    @Override
    protected void setTargetFromScheduleBundle(ThingEvent event, EventScheduleBundle<Asset> bundle) {
        event.setAsset(bundle.getTarget());
    }

    @Override
    protected void doSaveSchedule(ThingEvent openEvent) {
        nextEventScheduleService.createNextSchedule(openEvent);
    }

    @Override
    protected EventResult adjustEventResult(ThingEvent event, EventResult eventResult, EventResultCalculator resultCalculator) {
        for (SubEvent subEvent : event.getSubEvents()) {
            EventResult currentResult = resultCalculator.findEventResult(subEvent);
            eventResult = resultCalculator.adjustStatus(eventResult, currentResult);
        }

        return eventResult;
    }

    @Override
    protected void preSaveEvent(ThingEvent event, FileDataContainer fileData) {
        setProofTestData(event, fileData);
        setOrderForSubEvents(event);
        confirmSubEventsAreAgainstAttachedSubAssets(event);
    }

    @Override
    protected void postSaveEvent(ThingEvent event, FileDataContainer fileData) {
        updateAsset(event);
        uploadProofTestFile(event, fileData);
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    private void setProofTestData(ThingEvent event, FileDataContainer fileData) {
        if (fileData == null || fileData.getFileType() == null) {
            return;
        }

        if (event.getThingEventProofTests() == null) {
            event.setThingEventProofTests(new HashSet());
        }

        ThingEventProofTest thingEventProofTest = new ThingEventProofTest();
        thingEventProofTest.copyDataFrom(fileData);
        thingEventProofTest.setThingEvent(event);

        Iterator<ThingEventProofTest> itr = event.getThingEventProofTests().iterator();
        if(itr.hasNext()){
            itr.next().copyDataFrom(thingEventProofTest);
        }
        else {
            event.getThingEventProofTests().add(thingEventProofTest);
        }
    }


    private void setOrderForSubEvents(ThingEvent event) {
        Asset asset = persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getAsset().getId());
        List<SubAsset> subAssets = assetService.findSubAssets(asset);
        List<SubEvent> reorderedSubEvents = new ArrayList<SubEvent>();
        for (SubAsset subAsset : subAssets) {
            for (SubEvent subEvent : event.getSubEvents()) {
                if (subEvent.getAsset().equals(subAsset.getAsset())) {
                    reorderedSubEvents.add(subEvent);
                }
            }
        }
        event.setSubEvents(reorderedSubEvents);
    }

    private void confirmSubEventsAreAgainstAttachedSubAssets(ThingEvent event) throws UnknownSubAsset {
        Asset asset = persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getAsset().getId());
        List<SubAsset> subAssets = assetService.findSubAssets(asset);
        for (SubEvent subEvent : event.getSubEvents()) {
            if (!subAssets.contains(new SubAsset(subEvent.getAsset(), null))) {
                throw new UnknownSubAsset("asset id " + subEvent.getAsset().getId() + " is not attached to asset " + asset.getId());
            }
        }
    }

    private void updateAsset(ThingEvent event) {
        User modifiedBy = getCurrentUser();
        Asset asset = persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getAsset().getId());
        asset.setSubAssets(assetService.findSubAssets(asset));

        // pushes the location and the ownership to the asset based on the
        // events data.
        ownershipUpdates(event, asset);
        statusUpdates(event, asset);
        assignedToUpdates(event, asset);
        gpsUpdates(event, asset);

        try {
            assetService.update(asset, modifiedBy);
        } catch (SubAssetUniquenessException e) {
            logger.error("received a subasset uniquness error this should not be possible form this type of update.", e);
            throw new RuntimeException(e);
        }
    }

    public void updateAssetOwner(ThingEvent event) {
        User modifiedBy = getCurrentUser();
        Asset asset = persistenceService.findUsingTenantOnlySecurityWithArchived(Asset.class, event.getAsset().getId());
        asset.setSubAssets(assetService.findSubAssets(asset));


        ownershipUpdates(event, asset);

        try {
            assetService.update(asset, modifiedBy);
        } catch (SubAssetUniquenessException e) {
            logger.error("received a subasset uniquness error this should not be possible form this type of update.", e);
            throw new RuntimeException(e);
        }
    }



    private void assignedToUpdates(Event event, Asset asset) {
        if (event.hasAssignToUpdate()) {
            asset.setAssignedUser(event.getAssignedTo().getAssignedUser());
        }
    }

    private void statusUpdates(ThingEvent event, Asset asset) {
        asset.setAssetStatus(event.getAssetStatus());
    }

    private void ownershipUpdates(Event event, Asset asset) {
        asset.setOwner(event.getOwner());
        asset.setAdvancedLocation(event.getAdvancedLocation());
    }

    private void gpsUpdates(Event event, Asset asset) {
        if (event.getGpsLocation() != null && event.getGpsLocation().isValid()) {
            logger.info("Valid GPS recieved during inspection. Updating Asset " + asset.getIdentifier());
            asset.setGpsLocation(event.getGpsLocation());
        }
    }

    @Override
    protected void preUpdateEvent(ThingEvent event, FileDataContainer fileData) {
        setProofTestData(event, fileData);
        uploadProofTestFile(event, fileData);
    }

    @Override
    protected void postUpdateEvent(ThingEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    private void uploadProofTestFile(ThingEvent event, FileDataContainer fileData) throws ProcessingProofTestException {
        if (fileData == null || fileData.getFileData() == null) {
            return;
        }

        try {
            S3Service s3Service = ServiceLocator.getS3Service();

            String fileName = fileData.getFileName();
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            String contentType = ContentTypeUtil.getContentType(fileName);
            if (fileData.getChart() != null) {
                s3Service.uploadAssetProofTestChart(fileData.getChart(), contentType, event.getAsset().getMobileGUID(), event.getMobileGUID());
            }

        } catch (AmazonClientException e) {
            logger.error("Failed while writing Proof Test files", e);
            throw new ProcessingProofTestException(e);
        }
    }

    public void assignNextEventInSeries(ThingEvent event, EventEnum eventEnum) {
        Event nextEvent = null;

        RecurringAssetTypeEvent recurringEvent = event.getRecurringEvent();

        if (recurringEvent != null && recurringEvent.getAutoAssign()) {
            nextEvent = eventScheduleService.getNextAvailableSchedule(event);

            if (eventEnum == EventEnum.PERFORM) {
                nextEvent.setAssignee(event.getPerformedBy());
            } else if (eventEnum == EventEnum.CLOSE) {
                nextEvent.setAssignee(event.getAssignee());
            }
        }
    }

}
