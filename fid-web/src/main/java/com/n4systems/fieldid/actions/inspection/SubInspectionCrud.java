package com.n4systems.fieldid.actions.inspection;

import static com.n4systems.fieldid.utils.CopyInspectionFactory.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.SubEvent;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.CopyInspectionFactory;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Event;
import com.n4systems.model.Asset;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SubInspectionCrud extends InspectionCrud {
	private static final long serialVersionUID = 1L;

	private String token;
	private Asset parentAsset;
	private MasterInspection masterInspectionHelper;
	private boolean currentInspectionNew = true;

	public SubInspectionCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyProductManager,
			AssetManager assetManager, InspectionScheduleManager inspectionScheduleManager) {

		super(persistenceManager, eventManager, userManager, legacyProductManager, assetManager, inspectionScheduleManager);
	}

	@Override
	protected void initMemberFields() {
		masterInspectionHelper = (MasterInspection) getSession().get(MasterInspectionCrud.SESSION_KEY);
		event = new Event();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterInspectionHelper = (MasterInspection) getSession().get(MasterInspectionCrud.SESSION_KEY);

		if (MasterInspection.matchingMasterInspection(masterInspectionHelper, token)) {
			if (uniqueId == 0) {
				event = CopyInspectionFactory.copyInspection(masterInspectionHelper.getInspection());
				if (masterInspectionHelper.isMainInspectionStored()) {
					currentInspectionNew = false;
				}
			} else {
				event = copyInspection(masterInspectionHelper.createInspectionFromSubInspection(masterInspectionHelper.getSubInspection(uniqueId)));
				if (uniqueId != null) {
					currentInspectionNew = false;
				}
			}
			if (currentInspectionNew) {
				event.setAsset(null);
				event.setType(null);
			} else {
				parentAsset = masterInspectionHelper.getInspection().getAsset();

				setType(event.getType().getId());
				setAssetId(event.getAsset().getId());
			}
		} else {
			masterInspectionHelper = null;
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doAdd() {
		if (masterInspectionHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		reattachUploadedFiles();
		event.setId(null);
		if (event.getResults() != null) {
			restoreCriteriaResultsFromStoredInspection();
		}
		
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		Asset masterAsset = persistenceManager.find(Asset.class, masterInspectionHelper.getMasterAsset().getId(), getSecurityFilter(), "type.subTypes");
		masterAsset = assetManager.fillInSubAssetsOnAsset(masterAsset);
		masterInspectionHelper.setMasterAsset(masterAsset);

		if (currentInspectionNew) {
			return super.doAdd();
		}

		setUpSupportedProofTestTypes();
		encodeInfoOptionMapForUseInForm();
		setUpAssignTo();
		event.setAssetStatus(masterInspectionHelper.getAssetStatus());
		
		setScheduleId(masterInspectionHelper.getScheduleId());
		reattachUploadedFiles();

		getModifiableInspection().updateValuesToMatch(event);
		
		
		getNextSchedules().addAll(masterInspectionHelper.getNextSchedules());
		
		return SUCCESS;
	}

	private void setUpAssignTo() {
		setAssignedToId(masterInspectionHelper.getAssignedToId());
		setAssignToSomeone(masterInspectionHelper.isAssignToSomeone());
		
	}

	private void restoreCriteriaResultsFromStoredInspection() {
		criteriaResults = new ArrayList<CriteriaResult>();

		List<CriteriaSection> availbleSections = getInspectionFormHelper().getAvailableSections(event);

		for (CriteriaSection criteriaSection : availbleSections) {
			for (Criteria criteria : criteriaSection.getCriteria()) {
				boolean found = false;
				for (CriteriaResult result : event.getResults()) {
					if (result.getCriteria().equals(criteria)) {
						criteriaResults.add(result);
						found = true;
						break;
					}
				}
				if (!found) {
					criteriaResults.add(null);
				}
			}
		}
	}

	private void reattachUploadedFiles() {
		if (uniqueID != null) {
			List<FileAttachment> uploadedFiles;
			if (uniqueID == 0) {
				uploadedFiles = masterInspectionHelper.getUploadedFiles();
			} else {
				uploadedFiles = masterInspectionHelper.getSubInspectionUploadedFiles().get(masterInspectionHelper.getSubInspection(uniqueID));
			}

			if (uploadedFiles != null) {
				setUploadedFiles(uploadedFiles);
			}
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doEdit() {
		if (masterInspectionHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		reattachUploadedFiles();
		setScheduleId(masterInspectionHelper.getScheduleId());
		
		getModifiableInspection().updateValuesToMatch(event);
		return super.doEdit();
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doStoreNewSubInspection() {
		return storeSubInspection();
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doStoreExistingSubInspection() {
		return storeSubInspection();
	}
	
	public String storeSubInspection() {
		if (masterInspectionHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}

		event.setTenant(getTenant());
		event.setAsset(asset);
		getModifiableInspection().pushValuesTo(event);

		User modifiedBy = fetchCurrentUser();

		SubEvent subEvent = masterInspectionHelper.createSubInspectionFromInspection(event);
		subEvent.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));

		if (!masterInspectionHelper.getInspection().isNew()) {
			updateAttachmentList(event, modifiedBy);
		}

		if (uniqueID == null) {
			subEvent.syncFormVersionWithType();

			if (subEvent.isEditable()) {
				inspectionHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy);
			}

			masterInspectionHelper.addSubInspection(subEvent);
		} else {
			subEvent.setId(uniqueID);

			if (subEvent.isEditable()) {
				inspectionHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy);
			}

			masterInspectionHelper.replaceSubInspection(subEvent);
		}

		masterInspectionHelper.getSubInspectionUploadedFiles().put(subEvent, getUploadedFiles());

		addFlashMessageText("message.eventstored");

		return SUCCESS;
	}

	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doStoreNewMasterInspection() {
		return storeMasterInspection();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doStoreExistingMasterInspection() {
		return storeMasterInspection();
	}
	
	public String storeMasterInspection() {
		if (masterInspectionHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		
	
		User modifiedBy = fetchCurrentUser();

		try {
			findInspectionBook();
			processProofTestFile();
			getModifiableInspection().pushValuesTo(event);
			masterInspectionHelper.setProofTestFile(fileData);
			masterInspectionHelper.setAssignToUpdate(getAssignedTo(), isAssignToSomeone());

			if (masterInspectionHelper.getInspection().isNew()) {
				event.setTenant(getTenant());
				event.setAsset(asset);
				event.syncFormVersionWithType();

				masterInspectionHelper.setAssetStatus(event.getAssetStatus());

				if (event.isEditable()) {
					inspectionHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy);
				}
					
				masterInspectionHelper.setNextSchedules(getNextSchedules());
				
			} else {
				if (event.isEditable()) {
					inspectionHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy);
				}

				updateAttachmentList(event, modifiedBy);
			}
		
			event.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));
			masterInspectionHelper.setSchedule(eventSchedule);
			masterInspectionHelper.setScheduleId(inspectionScheduleId);
			masterInspectionHelper.setUploadedFiles(getUploadedFiles());
			masterInspectionHelper.setInspection(event);

		} catch (ProcessingProofTestException e) {
			addActionErrorText("error.processingprooftest");
			return INPUT;
		} catch (ValidationException e) {
			return INPUT;
		} catch (PersistenceException e) {
			return ERROR;
		}

		addFlashMessageText("message.eventstoreds");

		return SUCCESS;
	}

	public Long getParentAssetId() {
		return (parentAsset != null) ? parentAsset.getId() : null;
	}

	protected void processProofTestFile() throws ProcessingProofTestException {
		super.processProofTestFile();

		if (fileData != null) {
			if (event.getProofTestInfo() == null) {
				event.setProofTestInfo(new ProofTestInfo());
			}
			event.getProofTestInfo().setPeakLoad(fileData.getPeakLoad());
			event.getProofTestInfo().setDuration(fileData.getTestDuration());
		}

	}

	public void setParentAssetId(Long assetId) {
		if (assetId == null) {
			parentAsset = null;
		} else if (parentAsset == null || !assetId.equals(parentAsset.getId())) {
			parentAsset = persistenceManager.find(Asset.class, assetId, getSecurityFilter(), "type.inspectionTypes", "infoOptions");
			parentAsset = assetManager.fillInSubAssetsOnAsset(parentAsset);
		}
	}

	public Asset getParentAsset() {
		return parentAsset;
	}

	public boolean isParentAsset() {
		return (parentAsset.getId().equals(asset.getId()));
	}

	public boolean isSubAsset() {
		return !isParentAsset();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MasterInspection getMasterInspection() {
		return masterInspectionHelper;
	}
	
	
	
	@Override
	public void setType(Long type) {
		event.setType(null);
		super.setType(type);
	}


}
