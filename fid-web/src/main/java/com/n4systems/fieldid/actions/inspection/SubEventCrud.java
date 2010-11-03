package com.n4systems.fieldid.actions.inspection;

import static com.n4systems.fieldid.utils.CopyEventFactory.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.helpers.MasterEvent;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.model.SubEvent;
import org.apache.struts2.interceptor.validation.SkipValidation;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Event;
import com.n4systems.model.Asset;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SubEventCrud extends EventCrud {
	private static final long serialVersionUID = 1L;

	private String token;
	private Asset parentAsset;
	private MasterEvent masterEventHelper;
	private boolean currentInspectionNew = true;

	public SubEventCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyProductManager,
			AssetManager assetManager, EventScheduleManager eventScheduleManager) {

		super(persistenceManager, eventManager, userManager, legacyProductManager, assetManager, eventScheduleManager);
	}

	@Override
	protected void initMemberFields() {
		masterEventHelper = (MasterEvent) getSession().get(MasterEventCrud.SESSION_KEY);
		event = new Event();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterEventHelper = (MasterEvent) getSession().get(MasterEventCrud.SESSION_KEY);

		if (MasterEvent.matchingMasterInspection(masterEventHelper, token)) {
			if (uniqueId == 0) {
				event = CopyEventFactory.copyInspection(masterEventHelper.getInspection());
				if (masterEventHelper.isMainInspectionStored()) {
					currentInspectionNew = false;
				}
			} else {
				event = copyInspection(masterEventHelper.createInspectionFromSubInspection(masterEventHelper.getSubInspection(uniqueId)));
				if (uniqueId != null) {
					currentInspectionNew = false;
				}
			}
			if (currentInspectionNew) {
				event.setAsset(null);
				event.setType(null);
			} else {
				parentAsset = masterEventHelper.getInspection().getAsset();

				setType(event.getType().getId());
				setAssetId(event.getAsset().getId());
			}
		} else {
			masterEventHelper = null;
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doAdd() {
		if (masterEventHelper == null) {
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

		Asset masterAsset = persistenceManager.find(Asset.class, masterEventHelper.getMasterAsset().getId(), getSecurityFilter(), "type.subTypes");
		masterAsset = assetManager.fillInSubAssetsOnAsset(masterAsset);
		masterEventHelper.setMasterAsset(masterAsset);

		if (currentInspectionNew) {
			return super.doAdd();
		}

		setUpSupportedProofTestTypes();
		encodeInfoOptionMapForUseInForm();
		setUpAssignTo();
		event.setAssetStatus(masterEventHelper.getAssetStatus());
		
		setScheduleId(masterEventHelper.getScheduleId());
		reattachUploadedFiles();

		getModifiableInspection().updateValuesToMatch(event);
		
		
		getNextSchedules().addAll(masterEventHelper.getNextSchedules());
		
		return SUCCESS;
	}

	private void setUpAssignTo() {
		setAssignedToId(masterEventHelper.getAssignedToId());
		setAssignToSomeone(masterEventHelper.isAssignToSomeone());
		
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
				uploadedFiles = masterEventHelper.getUploadedFiles();
			} else {
				uploadedFiles = masterEventHelper.getSubInspectionUploadedFiles().get(masterEventHelper.getSubInspection(uniqueID));
			}

			if (uploadedFiles != null) {
				setUploadedFiles(uploadedFiles);
			}
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doEdit() {
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		reattachUploadedFiles();
		setScheduleId(masterEventHelper.getScheduleId());
		
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
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}

		event.setTenant(getTenant());
		event.setAsset(asset);
		getModifiableInspection().pushValuesTo(event);

		User modifiedBy = fetchCurrentUser();

		SubEvent subEvent = masterEventHelper.createSubInspectionFromInspection(event);
		subEvent.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));

		if (!masterEventHelper.getInspection().isNew()) {
			updateAttachmentList(event, modifiedBy);
		}

		if (uniqueID == null) {
			subEvent.syncFormVersionWithType();

			if (subEvent.isEditable()) {
				eventHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy);
			}

			masterEventHelper.addSubInspection(subEvent);
		} else {
			subEvent.setId(uniqueID);

			if (subEvent.isEditable()) {
				eventHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy);
			}

			masterEventHelper.replaceSubInspection(subEvent);
		}

		masterEventHelper.getSubInspectionUploadedFiles().put(subEvent, getUploadedFiles());

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
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		
	
		User modifiedBy = fetchCurrentUser();

		try {
			findInspectionBook();
			processProofTestFile();
			getModifiableInspection().pushValuesTo(event);
			masterEventHelper.setProofTestFile(fileData);
			masterEventHelper.setAssignToUpdate(getAssignedTo(), isAssignToSomeone());

			if (masterEventHelper.getInspection().isNew()) {
				event.setTenant(getTenant());
				event.setAsset(asset);
				event.syncFormVersionWithType();

				masterEventHelper.setAssetStatus(event.getAssetStatus());

				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy);
				}
					
				masterEventHelper.setNextSchedules(getNextSchedules());
				
			} else {
				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy);
				}

				updateAttachmentList(event, modifiedBy);
			}
		
			event.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));
			masterEventHelper.setSchedule(eventSchedule);
			masterEventHelper.setScheduleId(inspectionScheduleId);
			masterEventHelper.setUploadedFiles(getUploadedFiles());
			masterEventHelper.setInspection(event);

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
			parentAsset = persistenceManager.find(Asset.class, assetId, getSecurityFilter(), "type.eventTypes", "infoOptions");
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

	public MasterEvent getMasterInspection() {
		return masterEventHelper;
	}
	
	
	
	@Override
	public void setType(Long type) {
		event.setType(null);
		super.setType(type);
	}


}
