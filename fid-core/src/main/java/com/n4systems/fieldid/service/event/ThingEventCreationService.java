package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventResultCalculator;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.tools.FileDataContainer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        saveProofTestFiles(event, fileData);
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    private void setProofTestData(ThingEvent event, FileDataContainer fileData) {
        if (fileData == null) {
            return;
        }

        if (event.getProofTestInfo() == null) {
            event.setProofTestInfo(new ProofTestInfo());
        }

        event.getProofTestInfo().setProofTestType(fileData.getFileType());
        event.getProofTestInfo().setDuration(fileData.getTestDuration());
        event.getProofTestInfo().setPeakLoad(fileData.getPeakLoad());
        event.getProofTestInfo().setPeakLoadDuration(fileData.getPeakLoadDuration());
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
        saveProofTestFiles(event, fileData);
    }

    @Override
    protected void postUpdateEvent(ThingEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
    }

    private void saveProofTestFiles(Event event, FileDataContainer fileData) throws ProcessingProofTestException {
        if (fileData == null) {
            return;
        }
        File proofTestFile = PathHandler.getProofTestFile(event);
        File chartImageFile = PathHandler.getChartImageFile(event);

        // we should make sure our parent directories exist first
        proofTestFile.getParentFile().mkdirs();
        chartImageFile.getParentFile().mkdirs();

        try {
            if (fileData.getFileData() != null) {
                FileUtils.writeByteArrayToFile(proofTestFile, fileData.getFileData());
            } else if (proofTestFile.exists()) {
                proofTestFile.delete();
            }

            if (fileData.getChart() != null) {
                FileUtils.writeByteArrayToFile(chartImageFile, fileData.getChart());
            } else if (chartImageFile.exists()) {
                chartImageFile.delete();
            }

        } catch (IOException e) {
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
