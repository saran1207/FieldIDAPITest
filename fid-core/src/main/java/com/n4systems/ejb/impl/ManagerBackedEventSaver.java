package com.n4systems.ejb.impl;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.tools.FileDataContainer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

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

	public Event createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		// if the event has no group, lets create a new one now
		if (parameterObject.event.getGroup() == null) {
			parameterObject.event.setGroup(new EventGroup());
			parameterObject.event.getGroup().setTenant(parameterObject.event.getTenant());
			persistenceManager.save(parameterObject.event.getGroup(), parameterObject.userId);
		}

        Status calculatedStatus = calculateEventResultAndScore(parameterObject.event);
		if (parameterObject.event.getStatus() == null) {
            parameterObject.event.setStatus(calculatedStatus);
		}
		
		setProofTestData(parameterObject.event, parameterObject.fileData);
	
		confirmSubEventsAreAgainstAttachedSubAssets(parameterObject.event);

		setOrderForSubEvents(parameterObject.event);

        Date completedDate = parameterObject.event.getSchedule() == null ? null : parameterObject.event.getDate();

        findOrCreateSchedule(parameterObject.event, parameterObject.scheduleId);

        if (parameterObject.event.getSchedule() == null) {
            EventSchedule schedule = new EventSchedule();
            schedule.copyDataFrom(parameterObject.event);
            parameterObject.event.setSchedule(schedule);
        }

        parameterObject.event.setDate(completedDate);

        parameterObject.event.setEventState(Event.EventState.COMPLETED);

        Map<Long, byte[]> rememberedSignatureImages = collectSignatureImageData(parameterObject.event);

        if (parameterObject.event.getId() != null) {
            parameterObject.event = persistenceManager.update(parameterObject.event, parameterObject.userId);
        } else {
            persistenceManager.save(parameterObject.event, parameterObject.userId);
        }

		updateAsset(parameterObject.event, parameterObject.userId);
		
		// writeSignatureImagesToDisk MUST be called after persistenceManager.save(parameterObject.event, parameterObject.userId) as an 
		// event id is required to build the save path
		writeSignatureImagesToDisk(parameterObject.event, rememberedSignatureImages);
			
		saveProofTestFiles(parameterObject.event, parameterObject.fileData);
	
		processUploadedFiles(parameterObject.event, parameterObject.uploadedFiles);

        // Remove after mobile doesn't care about schedules
        // Do this last, as it can throw an exception if the schedule is in an invalid state.
        parameterObject.event.getSchedule().completed(parameterObject.event);
        persistenceManager.update(parameterObject.event.getSchedule());

		return parameterObject.event;
	}

    private Map<Long, byte[]> collectSignatureImageData(Event event) {
        Map<Long,byte[]> rememberedSignatures = new HashMap<Long, byte[]>();
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult instanceof SignatureCriteriaResult) {
                SignatureCriteriaResult signatureResult = (SignatureCriteriaResult) criteriaResult;
                if (signatureResult.isSigned() && signatureResult.getImage() != null) {
                    rememberedSignatures.put(signatureResult.getCriteria().getId(), signatureResult.getImage());
                }
            }
        }
        return rememberedSignatures;
    }
    
    private EventSchedule findOrCreateSchedule(Event event, Long scheduleId) {
        EventSchedule eventSchedule = null;

        if (scheduleId == null) {
            scheduleId = 0L;
        }

        if (scheduleId == -1) {
            // This means the user selected 'create new schedule'
            // Basically we just want the placeholder schedule with 1 change -- pretend it was scheduled for now (nextDate is completedDate)
            eventSchedule = new EventSchedule();
            eventSchedule.copyDataFrom(event);
            eventSchedule.setNextDate(event.getDate());
            persistenceManager.save(eventSchedule);
            event.setSchedule(eventSchedule);
        } else if (scheduleId > 0) {
            // There was an existing schedule selected.
            eventSchedule = persistenceManager.find(EventSchedule.class, scheduleId, event.getTenant());
            if (eventSchedule == null || eventSchedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED) {
                event.setSchedule(null);
            } else {
                event.setSchedule(eventSchedule);
            }
        }
        return eventSchedule;
    }
	
	private void writeSignatureImagesToDisk(Event event, Map<Long, byte[]> rememberedSignatureImages) {
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
                ((SignatureCriteriaResult)result).setImage(rememberedSignatureImage);
				try {
					sigService.storeSignatureFileFor((SignatureCriteriaResult)result);
				} catch (IOException e) {
					throw new FileAttachmentException("Unable to store signature image for result [" + result + "]", e);
				}
			}
		}
    }

	public Event updateEvent(Event event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		setProofTestData(event, fileData);
		updateDeficiencies(event.getResults());

        calculateEventResultAndScore(event);
		
		// writeSignatureImagesToDisk MUST be called prior persistenceManager.update(event, userId) as the image data in the 
		// signatures is transient and won't be there afterwards
		writeSignatureImagesToDisk(event, collectSignatureImageData(event));
		
		event = persistenceManager.update(event, userId);

        // TODO: Schedule is deprecated and these fields are now stored in the event (and editable there).

//        event.getSchedule().setCompletedDate(event.getDate());
//        event.getSchedule().setOwner(event.getOwner());
//        event.getSchedule().setAdvancedLocation(event.getAdvancedLocation());

//        persistenceManager.update(event.getSchedule(), userId);
        
//        EventSchedule newEventSchedule = null;
//        if (!event.getSchedule().wasScheduled()) {
//            if (scheduleId == -1) {
//                EventSchedule oldSchedule = event.getSchedule();
//
//                newEventSchedule = new EventSchedule(event);
//                persistenceManager.save(newEventSchedule);
//
//                newEventSchedule.getAsset().touch();
//                persistenceManager.update(newEventSchedule.getAsset());
//                persistenceManager.update(event);
//                persistenceManager.delete(oldSchedule);
//            } else if (scheduleId > 0 && !scheduleId.equals(event.getSchedule().getId())) {
//                newEventSchedule = persistenceManager.find(EventSchedule.class, scheduleId);
//                newEventSchedule.completed(event);
//                persistenceManager.update(newEventSchedule);
//                persistenceManager.update(event);
//            }
//        }

		updateAssetLastEventDate(event.getAsset());
		event.setAsset(persistenceManager.update(event.getAsset()));
		saveProofTestFiles(event, fileData);
		processUploadedFiles(event, uploadedFiles);
		
		updateScheduleOwnerShip(event, userId);
		return event;
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
		Asset asset = persistenceManager.find(Asset.class, event.getAsset().getId());
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		for (SubEvent subEvent : event.getSubEvents()) {
			if (!asset.getSubAssets().contains(new SubAsset(subEvent.getAsset(), null))) {
				throw new UnknownSubAsset("asset id " + subEvent.getAsset().getId() + " is not attached to asset " + asset.getId());
			}
		}
	}
	
	private void setOrderForSubEvents(Event event) {
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

	private Status calculateEventResultAndScore(Event event) {
        EventResultCalculator resultCalculator = new EventResultCalculator();
		Status eventResult = resultCalculator.findEventResult(event);

		for (SubEvent subEvent : event.getSubEvents()) {
            Status currentResult = resultCalculator.findEventResult(subEvent);
            eventResult = resultCalculator.adjustStatus(eventResult, currentResult);
		}

		return eventResult;
	}
	
	private void updateAsset(Event event, Long modifiedById) {
		User modifiedBy = em.find(User.class, modifiedById);
		Asset asset = em.find(Asset.class, event.getAsset().getId());

		updateAssetLastEventDate(asset);

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

	private void statusUpdates(Event event, Asset asset) {
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

	private void updateScheduleOwnerShip(Event event, Long userId) {
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
	
	
	private void processUploadedFiles(Event event, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		attachUploadedFiles(event, null, uploadedFiles);

		for (SubEvent subEvent : event.getSubEvents()) {
			attachUploadedFiles(event, subEvent, null);
		}

		event = persistenceManager.update(event);
	}

	Event attachUploadedFiles(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
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
					EventManagerImpl.logger.error("failed to copy uploaded file ", e);
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

	/*
	 * Writes the file data and chart image back onto the file system from a
	 * fully constructed FileDataContainer
	 */
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

	public Asset updateAssetLastEventDate(Asset asset) {
		asset.setLastEventDate(lastEventDateFinder.findLastEventDate(asset));
		return asset;
	}


	/**
	 * This must be called AFTER the event and sub-event have been persisted
	 */
	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		event = attachUploadedFiles(event, subEvent, uploadedFiles);
		return persistenceManager.update(event);
	}


}