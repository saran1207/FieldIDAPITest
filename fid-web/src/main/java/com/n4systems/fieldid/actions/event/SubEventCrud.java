package com.n4systems.fieldid.actions.event;

import static com.n4systems.fieldid.utils.CopyEventFactory.*;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventCreationService;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModelConverter;
import com.n4systems.fieldid.actions.exceptions.PersistenceException;
import com.n4systems.fieldid.actions.exceptions.ValidationException;
import com.n4systems.fieldid.actions.helpers.MasterEvent;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.CopyEventFactory;
import com.n4systems.model.Asset;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Status;
import com.n4systems.model.SubEvent;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;

public class SubEventCrud extends EventCrud {
	private static final long serialVersionUID = 1L;

	private String token;
	private Asset parentAsset;
	private MasterEvent masterEventHelper;
	private boolean currentEventNew = true;
    private String overrideResult;

	public SubEventCrud(PersistenceManager persistenceManager, EventManager eventManager, UserManager userManager, LegacyAsset legacyAssetManager,
			AssetManager assetManager, EventScheduleManager eventScheduleManager, EventCreationService eventCreationService,
            PersistenceService persistenceService) {

		super(persistenceManager, eventManager, userManager, legacyAssetManager, assetManager, eventScheduleManager, eventCreationService, persistenceService);
	}

	@Override
	protected void initMemberFields() {
		masterEventHelper = (MasterEvent) getSession().get(MasterEventCrud.SESSION_KEY);
		event = new Event();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterEventHelper = (MasterEvent) getSession().get(MasterEventCrud.SESSION_KEY);

		if (MasterEvent.matchingMasterEvent(masterEventHelper, token)) {
			if (uniqueId == 0) {
				event = CopyEventFactory.copyEvent(masterEventHelper.getEvent());
				if (masterEventHelper.isMainEventStored()) {
					currentEventNew = false;
				}
			} else {
				event = copyEvent(masterEventHelper.createEventFromSubEvent(masterEventHelper.getSubEvent(uniqueId)));
				if (uniqueId != null) {
					currentEventNew = false;
				}
			}
			if (currentEventNew) {
//				event.setAsset(null);
//				event.setType(null);
			} else {
				parentAsset = masterEventHelper.getEvent().getAsset();
                overrideResult = masterEventHelper.getOverrideResult();

				setType(event.getType().getId());
				setAssetId(event.getAsset().getId());
			}
		} else {
			masterEventHelper = null;
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doAdd() {
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		reattachUploadedFiles();
		event.setId(null);
		if (event.getResults() != null) {
			restoreCriteriaResultsFromStoredEvent();
		}
		
		try {
			testDependencies();
		} catch (MissingEntityException e) {
			return MISSING;
		}

		Asset masterAsset = persistenceManager.find(Asset.class, masterEventHelper.getMasterAsset().getId(), getSecurityFilter(), "type.subTypes");
		masterAsset = assetManager.fillInSubAssetsOnAsset(masterAsset);
		masterEventHelper.setMasterAsset(masterAsset);

		if (currentEventNew) {
			return super.doAdd();
		}

		setUpSupportedProofTestTypes();
		encodeInfoOptionMapForUseInForm();
		setUpAssignTo();
		event.setAssetStatus(masterEventHelper.getAssetStatus());
		
		setScheduleId(masterEventHelper.getScheduleId());
		reattachUploadedFiles();

		getModifiableEvent().updateValuesToMatch(event);
		
		
		getNextSchedules().addAll(masterEventHelper.getNextSchedules());
		
		return SUCCESS;
	}

	private void setUpAssignTo() {
		setAssignedToId(masterEventHelper.getAssignedToId());
		setAssignToSomeone(masterEventHelper.isAssignToSomeone());
		
	}

	private void restoreCriteriaResultsFromStoredEvent() {
		criteriaResults = new ArrayList<CriteriaResultWebModel>();

		List<CriteriaSection> availbleSections = getEventFormHelper().getAvailableSections(event);

        CriteriaResultWebModelConverter converter = new CriteriaResultWebModelConverter();

		for (CriteriaSection criteriaSection : availbleSections) {
			for (Criteria criteria : criteriaSection.getCriteria()) {
				boolean found = false;
				for (CriteriaResult result : event.getResults()) {
					if (result.getCriteria().equals(criteria)) {
						criteriaResults.add(converter.convertToWebModel(result, getSessionUser()));
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
				uploadedFiles = masterEventHelper.getSubEventUploadedFiles().get(masterEventHelper.getSubEvent(uniqueID));
			}

			if (uploadedFiles != null) {
				setUploadedFiles(uploadedFiles);
			}
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doEdit() {
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		reattachUploadedFiles();

		getModifiableEvent().updateValuesToMatch(event);
		return super.doEdit();
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doStoreNewSubEvent() throws Exception {
		return storeSubEvent();
	}
	
	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doStoreExistingSubEvent() throws Exception {
		return storeSubEvent();
	}
	
	public String storeSubEvent() throws Exception {
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}

		event.setTenant(getTenant());
		event.setAsset(asset);
		getModifiableEvent().pushValuesTo(event);
        if (overrideResult != null && !"auto".equals(overrideResult)) {
            event.setStatus(Status.valueOf(overrideResult));
        }

		User modifiedBy = fetchCurrentUser();

		SubEvent subEvent = masterEventHelper.createSubEventFromEvent(event);
		subEvent.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));

		if (!masterEventHelper.getEvent().isNew()) {
			updateAttachmentList(event, modifiedBy);
		}

		if (uniqueID == null) {
			if (subEvent.isEditable()) {
				eventHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy, getSessionUser());
                masterEventHelper.storeTemporaryFileIds(criteriaResults);
			}

			masterEventHelper.addSubEvent(subEvent);
		} else {
			subEvent.setId(uniqueID);

			if (subEvent.isEditable()) {
				eventHelper.processFormCriteriaResults(subEvent, criteriaResults, modifiedBy, getSessionUser());
                masterEventHelper.storeTemporaryFileIds(uniqueID, criteriaResults);
			}

			masterEventHelper.replaceSubEvent(subEvent);
		}

		masterEventHelper.getSubEventUploadedFiles().put(subEvent, getUploadedFiles());

		addFlashMessageText("message.eventstored");
		return SUCCESS;
	}

	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
	public String doStoreNewMasterEvent() throws Exception {
		return storeMasterEvent();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditEvent})
	public String doStoreExistingMasterEvent() throws Exception {
		return storeMasterEvent();
	}
	
	public String storeMasterEvent() throws Exception {
		if (masterEventHelper == null) {
			addActionErrorText("error.nomasterevent");
			return MISSING;
		}
		
	
		User modifiedBy = fetchCurrentUser();

		try {
			findEventBook();
			processProofTestFile();
			getModifiableEvent().pushValuesTo(event);
            setOverrideResult(getModifiableEvent().getOverrideResult());
			masterEventHelper.setProofTestFile(fileData);
			masterEventHelper.setAssignToUpdate(getAssignedTo(), isAssignToSomeone());
            masterEventHelper.setOverrideResult(getOverrideResult());

			if (masterEventHelper.getEvent().isNew()) {
				event.setTenant(getTenant());
				event.setAsset(asset);

				masterEventHelper.setAssetStatus(event.getAssetStatus());

				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());
                    masterEventHelper.storeTemporaryFileIds(0L, criteriaResults);
				}
					
				masterEventHelper.setNextSchedules(getNextSchedules());
				
			} else {
				if (event.isEditable()) {
					eventHelper.processFormCriteriaResults(event, criteriaResults, modifiedBy, getSessionUser());
                    masterEventHelper.storeTemporaryFileIds(0L, criteriaResults);
				}

				updateAttachmentList(event, modifiedBy);
			}
		
			event.setInfoOptionMap(decodeMapKeys(getEncodedInfoOptionMap()));
//			masterEventHelper.setSchedule(openEvent);
//			masterEventHelper.setScheduleId(eventScheduleId);
			masterEventHelper.setUploadedFiles(getUploadedFiles());
			masterEventHelper.setEvent(event);

		} catch (ProcessingProofTestException e) {
			addActionErrorText("error.processingprooftest");
			return INPUT;
		} catch (ValidationException e) {
			return INPUT;
		} catch (PersistenceException e) {
			return ERROR;
		}

		addFlashMessageText("message.eventstored");

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

	public MasterEvent getMasterEvent() {
		return masterEventHelper;
	}
	
	@Override
	public void setType(Long type) {
		event.setType(null);
		super.setType(type);
	}

	@Override
	public Long getEventId() {
		return masterEventHelper.getEvent().getId();
	}

    public String getOverrideResult() {
        return overrideResult;
    }

    public void setOverrideResult(String overrideResult) {
        this.overrideResult = overrideResult;
    }

    @Override
    public String getTemporarySignatureFileId(Long criteriaId) {
        return masterEventHelper.getTemporarySignatureFileId(uniqueID, criteriaId);
    }
}
