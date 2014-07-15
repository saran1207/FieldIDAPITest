package com.n4systems.ejb.impl;

import com.amazonaws.AmazonClientException;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.*;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.util.ContentTypeUtil;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class ManagerBackedEventSaver implements EventSaver {
	private final Logger logger = Logger.getLogger(ManagerBackedEventSaver.class);
	
	public final LegacyAsset legacyAssetManager;
	
	public final PersistenceManager persistenceManager;
	public final EntityManager em;
	public final LastEventDateFinder lastEventDateFinder;

	public ManagerBackedEventSaver(LegacyAsset legacyAssetManager, PersistenceManager persistenceManager,
			EntityManager em, LastEventDateFinder lastEventDateFinder) {
		this.legacyAssetManager = legacyAssetManager;
		this.persistenceManager = persistenceManager;
		this.em = em;
		this.lastEventDateFinder = lastEventDateFinder;
	}

	public ThingEvent createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
        EventResult calculatedEventResult = calculateEventResultAndScore(parameterObject.event);
		if (parameterObject.event.getEventResult() == null || parameterObject.event.getEventResult() == EventResult.VOID) {
            parameterObject.event.setEventResult(calculatedEventResult);
		}
		
		setProofTestData(parameterObject.event, parameterObject.fileData);
	
		confirmSubEventsAreAgainstAttachedSubAssets(parameterObject.event);

		setOrderForSubEvents(parameterObject.event);

        Date completedDate = parameterObject.event.getDate();

        //findOrCreateSchedule(parameterObject.event, parameterObject.scheduleId);

//        if (parameterObject.event.getSchedule() == null) {
//            EventSchedule schedule = new EventSchedule();
//            schedule.copyDataFrom(parameterObject.event);
//            parameterObject.event.setSchedule(schedule);
//        }

        parameterObject.event.setDate(completedDate);

        parameterObject.event.setWorkflowState(WorkflowState.COMPLETED);

        Map<Long, byte[]> rememberedSignatureImages = collectSignatureImageData(parameterObject.event);

        if (parameterObject.event.getId() != null) {
            parameterObject.event = persistenceManager.update(parameterObject.event, parameterObject.userId);
        } else {
            persistenceManager.save(parameterObject.event, parameterObject.userId);

            User user = persistenceManager.find(User.class, parameterObject.userId);

            if(user.isUsageBasedUser()) {
                int eventCount = parameterObject.event.getSubEvents().size() + 1;

                TenantSettings tenantSettings = user.getTenant().getSettings();
                tenantSettings.getUserLimits().setUsageBasedUserEvents(tenantSettings.getUserLimits().getUsageBasedUserEvents() - eventCount);
                persistenceManager.update(tenantSettings);
            }
        }

		updateAsset(parameterObject.event, parameterObject.userId);
		
		// writeSignatureImagesToDisk MUST be called after persistenceManager.save(parameterObject.event, parameterObject.userId) as an 
		// event id is required to build the save path
		writeSignatureImagesToDisk(parameterObject.event, rememberedSignatureImages);
			
		saveProofTestFiles(parameterObject.event, parameterObject.fileData);
	
		processUploadedFiles(parameterObject.event, parameterObject.uploadedFiles);

        // Remove after mobile doesn't care about schedules
        // Do this last, as it can throw an exception if the schedule is in an invalid state.
//        parameterObject.event.getSchedule().completed(parameterObject.event);
//        persistenceManager.update(parameterObject.event.getSchedule());

		return parameterObject.event;
	}

    private Map<Long, byte[]> collectSignatureImageData(ThingEvent event) {
        Map<Long,byte[]> rememberedSignatures = new HashMap<Long, byte[]>();
        addSignatureResultsFor(event, rememberedSignatures);
        for (SubEvent subEvent : event.getSubEvents()) {
            addSignatureResultsFor(subEvent, rememberedSignatures);
        }

        return rememberedSignatures;
    }

    private void addSignatureResultsFor(AbstractEvent<ThingEventType,Asset> event, Map<Long, byte[]> rememberedSignatures) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult instanceof SignatureCriteriaResult) {
                SignatureCriteriaResult signatureResult = (SignatureCriteriaResult) criteriaResult;
                if (signatureResult.isSigned() && signatureResult.getImage() != null) {
                    rememberedSignatures.put(signatureResult.getCriteria().getId(), signatureResult.getImage());
                }
            }
        }
    }

	private void writeSignatureImagesToDisk(ThingEvent event, Map<Long, byte[]> rememberedSignatureImages) {
		SignatureService sigService = new SignatureService();

        writeSignatureImagesFor(sigService, event.getResults(), rememberedSignatureImages);
        for (SubEvent subEvent : event.getSubEvents()) {
            writeSignatureImagesFor(sigService, subEvent.getResults(), rememberedSignatureImages);
        }
	}

    private void writeSignatureImagesFor(SignatureService sigService, Set<CriteriaResult> results, Map<Long, byte[]> rememberedSignatureImages) {
		for (CriteriaResult result : results) {
            byte[] rememberedSignatureImage = rememberedSignatureImages.get(result.getCriteria().getId());
            if (result.getCriteria().getCriteriaType() == CriteriaType.SIGNATURE && rememberedSignatureImage != null) {
                try {
                    SignatureCriteriaResult signatureResult = (SignatureCriteriaResult) result;
                    signatureResult.setImage(rememberedSignatureImage);
                    sigService.storeSignatureFileFor(signatureResult);
				} catch (IOException e) {
					throw new FileAttachmentException("Unable to store signature image for result [" + result + "]", e);
				}
			}
		}
    }

	public ThingEvent updateEvent(ThingEvent event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		setProofTestData(event, fileData);
		updateDeficiencies(event.getResults());

        calculateEventResultAndScore(event);
		
		// writeSignatureImagesToDisk MUST be called prior persistenceManager.update(event, userId) as the image data in the 
		// signatures is transient and won't be there afterwards
		writeSignatureImagesToDisk(event, collectSignatureImageData(event));
		
		event = persistenceManager.update(event, userId);

		event.setAsset(persistenceManager.update(event.getAsset()));
		saveProofTestFiles(event, fileData);
		processUploadedFiles(event, uploadedFiles);
		
		updateScheduleOwnerShip(event, userId);
		return event;
	}

	private void setProofTestData(ThingEvent event, FileDataContainer fileData) {
		if (fileData == null || fileData.getFileData() == null) {
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
	
	private void confirmSubEventsAreAgainstAttachedSubAssets(ThingEvent event) throws UnknownSubAsset {
		Asset asset = persistenceManager.find(Asset.class, event.getAsset().getId());
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		for (SubEvent subEvent : event.getSubEvents()) {
			if (!asset.getSubAssets().contains(new SubAsset(subEvent.getAsset(), null))) {
				throw new UnknownSubAsset("asset id " + subEvent.getAsset().getId() + " is not attached to asset " + asset.getId());
			}
		}
	}
	
	private void setOrderForSubEvents(ThingEvent event) {
		Asset asset = persistenceManager.find(Asset.class, event.getAsset().getId());
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		List<SubEvent> reorderedSubEvents = new ArrayList<SubEvent>();
		for (SubAsset subAsset : asset.getSubAssets()) {
			for (SubEvent subEvent : event.getSubEvents()) {
				if (subEvent.getAsset().equals(subAsset.getAsset())) {
					reorderedSubEvents.add(subEvent);
				}
			}
		}
		event.setSubEvents(reorderedSubEvents);
	}

	private EventResult calculateEventResultAndScore(ThingEvent event) {
        EventResultCalculator resultCalculator = new EventResultCalculator();
		EventResult eventResult = resultCalculator.findEventResult(event);

		for (SubEvent subEvent : event.getSubEvents()) {
            EventResult currentResult = resultCalculator.findEventResult(subEvent);
            eventResult = resultCalculator.adjustStatus(eventResult, currentResult);
		}

		return eventResult;
	}
	
	private void updateAsset(ThingEvent event, Long modifiedById) {
		User modifiedBy = em.find(User.class, modifiedById);
		Asset asset = em.find(Asset.class, event.getAsset().getId());

		// pushes the location and the ownership to the asset based on the
		// events data.
		ownershipUpdates(event, asset);
		statusUpdates(event, asset);
		assignedToUpdates(event, asset);
		gpsUpdates(event, asset);
		
		try {
			legacyAssetManager.update(asset, modifiedBy);
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

	private void ownershipUpdates(ThingEvent event, Asset asset) {
		asset.setOwner(event.getOwner());
		asset.setAdvancedLocation(event.getAdvancedLocation());
	}
	
	private void gpsUpdates(Event event, Asset asset) {
		if (event.getGpsLocation() != null && event.getGpsLocation().isValid()) {
			logger.info("Valid GPS recieved during inspection. Updating Asset " + asset.getIdentifier());
			asset.setGpsLocation(event.getGpsLocation());
		}
	}

	private void updateScheduleOwnerShip(ThingEvent event, Long userId) {
        event.setOwner(event.getOwner());
        event.setAdvancedLocation(event.getAdvancedLocation());
        new EventScheduleServiceImpl(persistenceManager).updateSchedule(event);
	}

	/**
	 * Updates changed Deficiencies on a list of CriteriaResults. Deficiencies
	 * are detected as being changed, iff their modified date is null. This
	 * should only be called on event update. If the deficiency is new
	 * create it.
	 * 
	 * @param results
	 *            The list of CriteriaResults to look for changed Deficiencies
	 *            on.
	 */
	private void updateDeficiencies(Set<CriteriaResult> results) {
		// walk through the results and deficiencies and update them if needed.
		for (CriteriaResult result : results) {
			CriteriaResult originalResult = persistenceManager.find(CriteriaResult.class, result.getId());
			originalResult.setDeficiencies(result.getDeficiencies());
			originalResult.setRecommendations(result.getRecommendations());
			persistenceManager.update(originalResult);
		}
	}
	
	
	private void processUploadedFiles(ThingEvent event, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		attachUploadedFiles(event, null, uploadedFiles);

		for (SubEvent subEvent : event.getSubEvents()) {
			attachUploadedFiles(event, subEvent, null);
		}

		event = persistenceManager.update(event);
	}

    ThingEvent attachUploadedFiles(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		AbstractEvent<ThingEventType,Asset> targetEvent;
		if (subEvent == null) {
			targetEvent = event;
		} else {
			targetEvent = subEvent;
		}
		File tmpDirectory = PathHandler.getTempRoot();
	
		if (uploadedFiles != null) {
	
			File tmpFile;
			// move and attach each uploaded file
			for (FileAttachment uploadedFile : uploadedFiles) {
	
				try {
                    S3Service s3Service = ServiceLocator.getS3Service();
					// move the file to it's new location, note that it's
					// location is currently relative to the tmpDirectory
					tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
					//FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                    uploadedFile.setTenant(targetEvent.getTenant());
                    uploadedFile.setModifiedBy(targetEvent.getModifiedBy());
                    uploadedFile.ensureMobileIdIsSet();
                    uploadedFile.setFileName(s3Service.getFileAttachmentPath(uploadedFile));
                    s3Service.uploadFileAttachment(tmpFile, uploadedFile);
	
					// clean up the temp file
					tmpFile.delete();

					// attach the attachment
					targetEvent.getAttachments().add(uploadedFile);
				} catch (AmazonClientException e) {
					EventManagerImpl.logger.error("failed to copy uploaded file ", e);
					throw new FileAttachmentException(e);
				}
	
			}
		}

		return event;
	}

	/*
	 * Writes the file data and chart image back onto the file system from a
	 * fully constructed FileDataContainer
	 */
	private void saveProofTestFiles(ThingEvent event, FileDataContainer fileData) throws ProcessingProofTestException {
		if (fileData == null || fileData.getFileData() == null) {
			return;
		}

		try {
            S3Service s3Service = ServiceLocator.getS3Service();

            if (fileData.getFileData() != null) {
                s3Service.uploadAssetProofTestFile(fileData.getFileData(), "text/plain", event.getAsset().getMobileGUID(), event.getMobileGUID());
            }
            if (fileData.getChart() != null) {
                s3Service.uploadAssetProofTestChart(fileData.getChart(), "image/png", event.getAsset().getMobileGUID(), event.getMobileGUID());
			}

		} catch (AmazonClientException e) {
			logger.error("Failed while writing Proof Test files", e);
			throw new ProcessingProofTestException(e);
		}

	}


	/**
	 * This must be called AFTER the event and sub-event have been persisted
	 */
	public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		event = attachUploadedFiles(event, subEvent, uploadedFiles);
		return persistenceManager.update(event);
	}

}
