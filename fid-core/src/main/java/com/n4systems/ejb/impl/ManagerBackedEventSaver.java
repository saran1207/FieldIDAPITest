package com.n4systems.ejb.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Status;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.tools.FileDataContainer;

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
		
		if (parameterObject.calculateEventResult) {
			parameterObject.event.setStatus(calculateEventResult(parameterObject.event));
		}
		
		
		setProofTestData(parameterObject.event, parameterObject.fileData);
	
		confirmSubEventsAreAgainstAttachedSubAssets(parameterObject.event);
	
		setOrderForSubEvents(parameterObject.event);
		
		persistenceManager.save(parameterObject.event, parameterObject.userId);
	
		updateAsset(parameterObject.event, parameterObject.userId);
	
	
		saveProofTestFiles(parameterObject.event, parameterObject.fileData);
	
		processUploadedFiles(parameterObject.event, parameterObject.uploadedFiles);
	
		return parameterObject.event;
	}

	public Event updateEvent(Event event, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		setProofTestData(event, fileData);
		updateDeficiencies(event.getResults());
		event = persistenceManager.update(event, userId);
		
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

	private Status calculateEventResult(Event event) {
		// determine result of the sections.
		Status eventResult = Status.NA;
		eventResult = findEventResult(event);
		for (SubEvent subEvent : event.getSubEvents()) {
			eventResult = adjustStatus(eventResult, findEventResult(subEvent));
			if (eventResult == Status.FAIL) {
				break;
			}
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

	private void updateScheduleOwnerShip(Event event, Long userId) {
		EventSchedule schedule = event.getSchedule();
		if (schedule != null) {
			schedule.setOwner(event.getOwner());
			schedule.setAdvancedLocation(event.getAdvancedLocation());
			new EventScheduleServiceImpl(persistenceManager).updateSchedule(schedule);
		}
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

	private Status findEventResult(AbstractEvent event) {
		Status eventResult = Status.NA;
		for (CriteriaResult result : event.getResults()) {
            if (result instanceof OneClickCriteriaResult) {
                OneClickCriteriaResult oneClickResult = (OneClickCriteriaResult) result;

                eventResult = adjustStatus(eventResult, oneClickResult.getResult());

                if (eventResult == Status.FAIL) {
                    break;
                }
            }

		}
		return eventResult;
	}

	private Status adjustStatus(Status currentStatus, Status newStatus) {
		if (newStatus == Status.FAIL) {
			currentStatus = Status.FAIL;

		} else if (newStatus == Status.PASS) {
			currentStatus = Status.PASS;
		}
		return currentStatus;
	}


	/**
	 * This must be called AFTER the event and sub-event have been persisted
	 */
	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		event = attachUploadedFiles(event, subEvent, uploadedFiles);
		return persistenceManager.update(event);
	}

	
	

}