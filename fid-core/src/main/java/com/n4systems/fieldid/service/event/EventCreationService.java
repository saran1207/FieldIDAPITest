package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventResultCalculator;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.*;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.tools.FileDataContainer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class EventCreationService extends FieldIdPersistenceService {
    
    private static final Logger logger = Logger.getLogger(EventCreationService.class);

    @Autowired
    private AssetService assetService;

    @Autowired
    private LastEventDateService lastEventDateService;

    @Autowired
    private NextEventScheduleService nextEventScheduleService;

	@Autowired
	private S3Service s3Service;

    @Transactional
    public Event createEventWithSchedules(Event event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, List<EventScheduleBundle> schedules) {
        Event savedEvent = createEvent(event, scheduleId, fileData, uploadedFiles);
        for (EventScheduleBundle eventScheduleBundle : schedules) {
            Event openEvent = new Event();
            openEvent.setTenant(eventScheduleBundle.getAsset().getTenant());
            openEvent.setAsset(eventScheduleBundle.getAsset());
            openEvent.setType(eventScheduleBundle.getType());
            openEvent.setOwner(eventScheduleBundle.getAsset().getOwner());
            openEvent.setProject(eventScheduleBundle.getJob());
            openEvent.setNextDate(eventScheduleBundle.getScheduledDate());
            openEvent.getSchedule().copyDataFrom(event);
            nextEventScheduleService.createNextSchedule(openEvent);
        }
        return savedEvent;
    }

    @Transactional
    public Event createEvent(Event event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) {
        Status calculatedStatus = calculateEventResultAndScore(event);

        if (event.getStatus() == null) {
            event.setStatus(calculatedStatus);
        }

        User user = getCurrentUser();

        event.setEventState(Event.EventState.COMPLETED);

        setProofTestData(event, fileData);

        confirmSubEventsAreAgainstAttachedSubAssets(event);

        setOrderForSubEvents(event);

        Date completedDate = event.getDate();

        findOrCreateSchedule(event, scheduleId);

        event.setDate(completedDate);

        if (event.getId() == null) {
            persistenceService.save(event);
        } else {
            persistenceService.update(event);
        }

        for (CriteriaResult criteriaResult : event.getResults()) {
            for (Event action : criteriaResult.getActions()) {
                action.getSchedule().copyDataFrom(action);
                persistenceService.save(action);
                persistenceService.update(criteriaResult);
            }
        }

        updateAsset(event, user.getId());

        // writeSignatureImagesToDisk MUST be called after persistenceManager.save(parameterObject.event, parameterObject.userId) as an 
        // event id is required to build the save path
        writeSignatureImagesToDisk(event);
		saveCriteriaResultImages(event);
        saveProofTestFiles(event, fileData);

        processUploadedFiles(event, uploadedFiles);

        // Do this last, as it can throw an exception if the schedule is in an invalid state.
        event.getSchedule().completed(event);
        persistenceService.update(event.getSchedule());

        return event;
    }

    private void processUploadedFiles(Event event, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
        attachUploadedFiles(event, null, uploadedFiles);

        for (SubEvent subEvent : event.getSubEvents()) {
            attachUploadedFiles(event, subEvent, null);
        }

        persistenceService.update(event);
    }

    private Event attachUploadedFiles(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
        File attachmentDirectory;
        AbstractEvent targetEvent;
        if (subEvent == null) {
            attachmentDirectory = PathHandler.getAttachmentFile(event);
            targetEvent = event;
        } else {
            attachmentDirectory = PathHandler.getAttachmentFile(event, subEvent);
            targetEvent = subEvent;
        }
        File tmpDirectory = PathHandler.getTempRoot();

        if (uploadedFiles != null) {

            File tmpFile;
            // move and attach each uploaded file
            for (FileAttachment uploadedFile : uploadedFiles) {

                try {
                    // move the file to it's new location, note that it's
                    // location is currently relative to the tmpDirectory
                    tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
                    FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);

                    // clean up the temp file
                    tmpFile.delete();

                    // now we need to set the correct file name for the
                    // attachment and set the modifiedBy
                    uploadedFile.setFileName(tmpFile.getName());
                    uploadedFile.setTenant(targetEvent.getTenant());
                    uploadedFile.setModifiedBy(targetEvent.getModifiedBy());

                    // attach the attachment
                    targetEvent.getAttachments().add(uploadedFile);
                } catch (IOException e) {
                    logger.error("failed to copy uploaded file ", e);
                    throw new FileAttachmentException(e);
                }

            }
        }

        // Now we need to cleanup any files that are no longer attached to the
        // event
        if (attachmentDirectory.exists()) {

            /*
                * We'll start by constructing a list of attached file names which
                * will be used in a directory filter
                */
            final List<String> attachedFiles = new ArrayList<String>();
            for (FileAttachment file : targetEvent.getAttachments()) {
                attachedFiles.add(file.getFileName());
            }

            /*
                * This lists all files in the attachment directory
                */
            for (File detachedFile : attachmentDirectory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    // accept only files that are not in our attachedFiles list
                    return !attachedFiles.contains(name);
                }
            })) {
                /*
                     * any file returned from our fileNotAttachedFilter, is not in
                     * our attached file list and should be removed
                     */
                detachedFile.delete();

            }
        }
        return event;
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

    private Status calculateEventResultAndScore(Event event) {
        EventResultCalculator resultCalculator = new EventResultCalculator();
        Status eventResult = resultCalculator.findEventResult(event);

        for (SubEvent subEvent : event.getSubEvents()) {
            Status currentResult = resultCalculator.findEventResult(subEvent);
            eventResult = resultCalculator.adjustStatus(eventResult, currentResult);
        }

        return eventResult;
    }

    private void setProofTestData(Event event, FileDataContainer fileData) {
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

    private void confirmSubEventsAreAgainstAttachedSubAssets(Event event) throws UnknownSubAsset {
        Asset asset = persistenceService.find(Asset.class, event.getAsset().getId());
        List<SubAsset> subAssets = assetService.findSubAssets(asset);
        for (SubEvent subEvent : event.getSubEvents()) {
            if (!subAssets.contains(new SubAsset(subEvent.getAsset(), null))) {
                throw new UnknownSubAsset("asset id " + subEvent.getAsset().getId() + " is not attached to asset " + asset.getId());
            }
        }
    }

    private void setOrderForSubEvents(Event event) {
        Asset asset = persistenceService.find(Asset.class, event.getAsset().getId());
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

    private EventSchedule findOrCreateSchedule(Event event, Long scheduleId) {
        EventSchedule eventSchedule = null;
        if (scheduleId == -1) {
            // This means the user selected 'create new schedule'
            // Basically we just want the placeholder schedule with 1 change -- pretend it was scheduled for now (nextDate is completedDate)
            eventSchedule = new EventSchedule();
            eventSchedule.copyDataFrom(event);
            eventSchedule.setNextDate(event.getDate());
            persistenceService.save(eventSchedule);
            event.setSchedule(eventSchedule);
        } else if (scheduleId > 0) {
            // There was an existing schedule selected.
            eventSchedule = persistenceService.find(EventSchedule.class, scheduleId);
            if (eventSchedule == null || eventSchedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED) {
                event.setSchedule(null);
            } else {
                event.setSchedule(eventSchedule);
            }
        }
        return eventSchedule;
    }

    private void updateAsset(Event event, Long modifiedById) {
        User modifiedBy = getCurrentUser();
        Asset asset = persistenceService.find(Asset.class, event.getAsset().getId());
        asset.setSubAssets(assetService.findSubAssets(asset));

        updateAssetLastEventDate(asset);

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

    private void statusUpdates(Event event, Asset asset) {
        asset.setAssetStatus(event.getAssetStatus());
    }

    private void ownershipUpdates(Event event, Asset asset) {
        asset.setOwner(event.getOwner());
        asset.setAdvancedLocation(event.getAdvancedLocation());
    }

    public Asset updateAssetLastEventDate(Asset asset) {
        asset.setLastEventDate(lastEventDateService.findLastEventDate(asset));
        return asset;
    }

    private void writeSignatureImagesToDisk(Event event) {
        SignatureService sigService = new SignatureService();

        writeSignatureImagesFor(sigService, event.getResults());
        for (SubEvent subEvent : event.getSubEvents()) {
            writeSignatureImagesFor(sigService, subEvent.getResults());
        }
    }

    private void writeSignatureImagesFor(SignatureService sigService, Set<CriteriaResult> results) {
        for (CriteriaResult result : results) {
            if (result.getCriteria().getCriteriaType() == CriteriaType.SIGNATURE && ((SignatureCriteriaResult)result).hasImageInMemoryOrTemporaryFile()) {
                try {
                    sigService.storeSignatureFileFor((SignatureCriteriaResult)result);
                } catch (IOException e) {
                    throw new FileAttachmentException("Unable to store signature image for result [" + result + "]", e);
                }
            }
        }
    }

    private void gpsUpdates(Event event, Asset asset) {
        if (event.getGpsLocation() != null && event.getGpsLocation().isValid()) {
            logger.info("Valid GPS recieved during inspection. Updating Asset " + asset.getIdentifier());
            asset.setGpsLocation(event.getGpsLocation());
        }
    }

    @Transactional
    public void updateEvent(Event event) {
        persistenceService.update(event);
    }

	private void saveCriteriaResultImages(Event event) {
		saveCriteriaResultImages(event.getResults());
		for (SubEvent subEvent: event.getSubEvents()) {
			saveCriteriaResultImages(subEvent.getResults());
		}
	}

	private void saveCriteriaResultImages(Collection<CriteriaResult> results) {
		for (CriteriaResult result: results) {
			for (CriteriaResultImage criteriaResultImage: result.getCriteriaImages()) {
				s3Service.uploadCriteriaResultImage(criteriaResultImage);
			}
		}
	}

}
