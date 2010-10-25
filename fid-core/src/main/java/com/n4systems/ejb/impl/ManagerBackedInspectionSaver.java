package com.n4systems.ejb.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Status;
import com.n4systems.model.SubInspection;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.tools.FileDataContainer;

public class ManagerBackedInspectionSaver implements InspectionSaver {
	private final Logger logger = Logger.getLogger(ManagerBackedInspectionSaver.class);
	
	public final LegacyProductSerial legacyProductManager;
	
	public final PersistenceManager persistenceManager;
	public final EntityManager em;
	public final LastInspectionDateFinder lastInspectionDateFinder;

	

	public ManagerBackedInspectionSaver(LegacyProductSerial legacyProductManager, PersistenceManager persistenceManager, 
			EntityManager em, LastInspectionDateFinder lastInspectionDateFinder) {
		super();
		this.legacyProductManager = legacyProductManager;
		this.persistenceManager = persistenceManager;
		this.em = em;
		this.lastInspectionDateFinder = lastInspectionDateFinder;
	}

	public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		// if the inspection has no group, lets create a new one now
		if (parameterObject.inspection.getGroup() == null) {
			parameterObject.inspection.setGroup(new InspectionGroup());
			parameterObject.inspection.getGroup().setTenant(parameterObject.inspection.getTenant());
			persistenceManager.save(parameterObject.inspection.getGroup(), parameterObject.userId);
		}
		
		if (parameterObject.calculateInspectionResult) {
			parameterObject.inspection.setStatus(calculateInspectionResult(parameterObject.inspection));
		}
		
		
		setProofTestData(parameterObject.inspection, parameterObject.fileData);
	
		confirmSubInspectionsAreAgainstAttachedSubAssets(parameterObject.inspection);
	
		setOrderForSubInspections(parameterObject.inspection);
		
		persistenceManager.save(parameterObject.inspection, parameterObject.userId);
	
		updateProduct(parameterObject.inspection, parameterObject.userId);
	
	
		saveProofTestFiles(parameterObject.inspection, parameterObject.fileData);
	
		processUploadedFiles(parameterObject.inspection, parameterObject.uploadedFiles);
	
		return parameterObject.inspection;
	}

	
	

	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		setProofTestData(inspection, fileData);
		updateDeficiencies(inspection.getResults());
		inspection = persistenceManager.update(inspection, userId);
		
		updateProductInspectionDate(inspection.getAsset());
		inspection.setAsset(persistenceManager.update(inspection.getAsset()));
		saveProofTestFiles(inspection, fileData);
		processUploadedFiles(inspection, uploadedFiles);
		
		updateScheduleOwnerShip(inspection, userId);
		return inspection;
	}

	private void setProofTestData(Inspection inspection, FileDataContainer fileData) {
		if (fileData == null) {
			return;
		}
	
		if (inspection.getProofTestInfo() == null) {
			inspection.setProofTestInfo(new ProofTestInfo());
		}
	
		inspection.getProofTestInfo().setProofTestType(fileData.getFileType());
		inspection.getProofTestInfo().setDuration(fileData.getTestDuration());
		inspection.getProofTestInfo().setPeakLoad(fileData.getPeakLoad());
		inspection.getProofTestInfo().setPeakLoadDuration(fileData.getPeakLoadDuration());
	}
	
	private void confirmSubInspectionsAreAgainstAttachedSubAssets(Inspection inspection) throws UnknownSubAsset {
		Asset asset = persistenceManager.find(Asset.class, inspection.getAsset().getId());
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		for (SubInspection subInspection : inspection.getSubInspections()) {
			if (!asset.getSubAssets().contains(new SubAsset(subInspection.getAsset(), null))) {
				throw new UnknownSubAsset("asset id " + subInspection.getAsset().getId() + " is not attached to asset " + asset.getId());
			}
		}
	}
	
	private void setOrderForSubInspections(Inspection inspection) {
		Asset asset = persistenceManager.find(Asset.class, inspection.getAsset().getId());
		asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		List<SubInspection> reorderedSubInspections = new ArrayList<SubInspection>();
		for (SubAsset subAsset : asset.getSubAssets()) {
			for (SubInspection subInspection : inspection.getSubInspections()) {
				if (subInspection.getAsset().equals(subAsset.getAsset())) {
					reorderedSubInspections.add(subInspection);
				}
			}
		}
		inspection.setSubInspections(reorderedSubInspections);
		
	}

	private Status calculateInspectionResult(Inspection inspection) {
		// determine result of the sections.
		Status inspectionResult = Status.NA;
		inspectionResult = findInspectionResult(inspection);
		for (SubInspection subInspection : inspection.getSubInspections()) {
			inspectionResult = adjustStatus(inspectionResult, findInspectionResult(subInspection));
			if (inspectionResult == Status.FAIL) {
				break;
			}
		}

		return inspectionResult;
	}
	
	
	private void updateProduct(Inspection inspection, Long modifiedById) {
		User modifiedBy = em.find(User.class, modifiedById);
		Asset asset = em.find(Asset.class, inspection.getAsset().getId());

		updateProductInspectionDate(asset);

		// pushes the location and the ownership to the asset based on the
		// inspections data.
		ownershipUpdates(inspection, asset);
		statusUpdates(inspection, asset);
		assignedToUpdates(inspection, asset);
		
		
		try {
			legacyProductManager.update(asset, modifiedBy);
		} catch (SubAssetUniquenessException e) {
			logger.error("received a subasset uniquness error this should not be possible form this type of update.", e);
			throw new RuntimeException(e);
		}

	}

	private void assignedToUpdates(Inspection inspection, Asset asset) {
		if (inspection.hasAssignToUpdate()) {
			asset.setAssignedUser(inspection.getAssignedTo().getAssignedUser());
		}
		
	}

	private void statusUpdates(Inspection inspection, Asset asset) {
		asset.setAssetStatus(inspection.getAssetStatus());
	}

	private void ownershipUpdates(Inspection inspection, Asset asset) {
		asset.setOwner(inspection.getOwner());
		asset.setAdvancedLocation(inspection.getAdvancedLocation());
	}
	
	
	
	private void updateScheduleOwnerShip(Inspection inspection, Long userId) {
		InspectionSchedule schedule = inspection.getSchedule();
		if (schedule != null) {
			schedule.setOwner(inspection.getOwner());
			schedule.setAdvancedLocation(inspection.getAdvancedLocation());
			new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(schedule);
		}
	}

	/**
	 * Updates changed Deficiencies on a list of CriteriaResults. Deficiencies
	 * are detected as being changed, iff their modified date is null. This
	 * should only be called on inspection update. If the deficiency is new
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
	
	
	private void processUploadedFiles(Inspection inspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		attachUploadedFiles(inspection, null, uploadedFiles);

		for (SubInspection subInspection : inspection.getSubInspections()) {
			attachUploadedFiles(inspection, subInspection, null);
		}

		inspection = persistenceManager.update(inspection);
	}

	Inspection attachUploadedFiles(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		File attachmentDirectory;
		AbstractInspection targetInspection;
		if (subInspection == null) {
			attachmentDirectory = PathHandler.getAttachmentFile(inspection);
			targetInspection = inspection;
		} else {
			attachmentDirectory = PathHandler.getAttachmentFile(inspection, subInspection);
			targetInspection = subInspection;
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
					uploadedFile.setTenant(targetInspection.getTenant());
					uploadedFile.setModifiedBy(targetInspection.getModifiedBy());
	
					// attach the attachment
					targetInspection.getAttachments().add(uploadedFile);
				} catch (IOException e) {
					InspectionManagerImpl.logger.error("failed to copy uploaded file ", e);
					throw new FileAttachmentException(e);
				}
	
			}
		}
	
		// Now we need to cleanup any files that are no longer attached to the
		// inspection
		if (attachmentDirectory.exists()) {
	
			/*
			 * We'll start by constructing a list of attached file names which
			 * will be used in a directory filter
			 */
			final List<String> attachedFiles = new ArrayList<String>();
			for (FileAttachment file : targetInspection.getAttachments()) {
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
		return inspection;
	}

	
	/*
	 * Writes the file data and chart image back onto the file system from a
	 * fully constructed FileDataContainer
	 */
	private void saveProofTestFiles(Inspection inspection, FileDataContainer fileData) throws ProcessingProofTestException {
		if (fileData == null) {
			return;
		}
		File proofTestFile = PathHandler.getProofTestFile(inspection);
		File chartImageFile = PathHandler.getChartImageFile(inspection);

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

	public Asset updateProductInspectionDate(Asset asset) {
		asset.setLastInspectionDate(lastInspectionDateFinder.findLastInspectionDate(asset));
		return asset;
	}

	private Status findInspectionResult(AbstractInspection inspection) {
		Status inspectionResult = Status.NA;
		for (CriteriaResult result : inspection.getResults()) {

			inspectionResult = adjustStatus(inspectionResult, result.getResult());

			if (inspectionResult == Status.FAIL) {
				break;
			}
		}
		return inspectionResult;
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
	 * This must be called AFTER the inspection and sub-inspection have been persisted
	 */
	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		inspection = attachUploadedFiles(inspection, subInspection, uploadedFiles);
		return persistenceManager.update(inspection);
	}

	
	

}