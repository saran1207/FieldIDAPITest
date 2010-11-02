package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.EventManager;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.InspectionScheduleBundle;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InspectionScheduleSuggestion;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.actions.inspection.viewmodel.WebInspectionScheduleToInspectionScheduleBundleConverter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.CopyInspectionFactory;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.handlers.creator.InspectionPersistenceFactory;
import com.n4systems.handlers.creator.inspections.factory.ProductionInspectionPersistenceFactory;
import com.n4systems.model.InspectionType;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


public class MasterInspectionCrud extends AbstractCrud {
	protected static final String SESSION_KEY = "masterInspection";
	private static Logger logger = Logger.getLogger(MasterInspectionCrud.class);
	private static final long serialVersionUID = 1L;

	private EventManager eventManager;
	private InspectionScheduleManager inspectionScheduleManager;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;

	private Event event;
	private EventGroup eventGroup;
	private Asset asset;
	private List<SubAssetHelper> subAssets;

	private MasterInspection masterInspection;
	private String token;
	private boolean dirtySession = true;

	private boolean cleanToInspectionsToMatchConfiguration = false;
	

	public MasterInspectionCrud(PersistenceManager persistenceManager, EventManager eventManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
		this.inspectionPersistenceFactory = new ProductionInspectionPersistenceFactory();
	}

	@Override
	protected void initMemberFields() {
		masterInspection = (MasterInspection) getSession().get(SESSION_KEY);

		if (masterInspection == null || token == null) {
			createNewMasterInspection();
		} else if (!MasterInspection.matchingMasterInspection(masterInspection, token)) {
			masterInspection = null;
			return;
		}

		if (eventGroup == null) {
			setInspectionGroupId(masterInspection.getInspectionGroupId());
		}
		event = masterInspection.getInspection();
	}

	private void createNewMasterInspection() {
		masterInspection = new MasterInspection();
		masterInspection.setInspection(new Event());
		token = masterInspection.getToken();
		masterInspection.getInspection().setAsset(asset);
		getSession().put("masterInspection", masterInspection);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterInspection = (MasterInspection) getSession().get(SESSION_KEY);

		if (masterInspection == null || token == null || !MasterInspection.matchingMasterInspection(masterInspection, token)) {
			Event event = eventManager.findAllFields(uniqueId, getSecurityFilter());
			masterInspection = new MasterInspection(event);
			if (event != null) {
				for (SubEvent i : event.getSubEvents()) {
					persistenceManager.reattchAndFetch(i, "asset.id", "results", "infoOptionMap", "type", "attachments");
				}
			}
		}

		if (masterInspection != null) {
			event = masterInspection.getInspection();
			token = masterInspection.getToken();
			setAssetId(masterInspection.getInspection().getAsset().getId());
			getSession().put("masterInspection", masterInspection);
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doAdd() {

		if (masterInspection == null) {
			addActionError(getText("error.nomasterevent"));
			return MISSING;
		}

		if (asset == null) {
			if (masterInspection.getInspection().getAsset() == null) {
				addActionError(getText("error.noasset"));
				return MISSING;
			} else {
				asset = masterInspection.getInspection().getAsset();
			}
		}

		if (masterInspection.getInspection() == null) {
			addActionError(getText("error.noevent"));
			return ERROR;
		}
		if (masterInspection.getInspection().getType() == null) {
			addActionError(getText("error.noinpsectiontype"));
			return MISSING;
		}

		event = masterInspection.getInspection();

		if (eventGroup != null) {
			masterInspection.setInspectionGroupId(eventGroup.getId());
		}

		if (masterInspection.getSchedule() != null) {
			masterInspection.getSchedule().inProgress();
			try {
				persistenceManager.update(masterInspection.getSchedule(), getSessionUser().getId());
				addActionMessageText("message.scheduleinprogress");
			} catch (Exception e) {
				logger.warn("could not move schedule to in progress", e);
			}
		}

		return SUCCESS;
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doEdit() {

		if (masterInspection == null) {
			addActionError(getText("error.nomasterevent"));
			return MISSING;
		}

		if (asset == null) {
			if (masterInspection.getInspection().getAsset() == null) {
				addActionError(getText("error.noasset"));
				return MISSING;
			} else {
				asset = masterInspection.getInspection().getAsset();
			}
		}

		if (masterInspection.getInspection() == null) {
			addActionError(getText("error.noevent"));
			return MISSING;
		}

		return SUCCESS;
	}

	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doCreate() {
		return save();
	}
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.EditInspection})
	public String doUpdate() {
		return save();
	}
	
	
	@Validations(requiredFields = { @RequiredFieldValidator(message = "", key = "error.mastereventnotcomplete", fieldName = "inspectionComplete") })
	private String save() {

		if (masterInspection == null) {
			return ERROR;
		}

		setInspectionGroupId(masterInspection.getInspectionGroupId());
		event.setGroup(eventGroup);

		try {
			if (uniqueID == null) {
				if (cleanToInspectionsToMatchConfiguration) {
					masterInspection.cleanSubInspectionsForNonValidSubAssets(asset);
				}
				Event master = CopyInspectionFactory.copyInspection(masterInspection.getCompletedInspection());
				
				
				CreateInspectionParameterBuilder createInspecitonBuiler = new CreateInspectionParameterBuilder(master, getSessionUserId())
						.withProofTestFile(masterInspection.getProofTestFile())
						.withUploadedImages(masterInspection.getUploadedFiles());
				
				
				
				createInspecitonBuiler.addSchedules(createInspectionScheduleBundles(masterInspection.getNextSchedules()));
				
				event = inspectionPersistenceFactory.createInspectionCreator().create(
						createInspecitonBuiler.build());
				uniqueID = event.getId();
			} else {
				Event master = CopyInspectionFactory.copyInspection(masterInspection.getCompletedInspection());
				event = eventManager.updateEvent(master, getSessionUser().getUniqueID(), masterInspection.getProofTestFile(), masterInspection.getUploadedFiles());
			}

			completeSchedule(masterInspection.getScheduleId(), masterInspection.getSchedule());

			for (int i = 0; i < masterInspection.getSubInspections().size(); i++) {
				SubEvent subEvent = new SubEvent();
				subEvent.setName("unknown");
				SubEvent uploadedFileKey = masterInspection.getSubInspections().get(i);
				try {
					subEvent = event.getSubEvents().get(i);

					event = eventManager.attachFilesToSubEvent(event, subEvent, masterInspection.getSubInspectionUploadedFiles().get(uploadedFileKey));

				} catch (Exception e) {
					addFlashError(getText("error.subeventfileupload", subEvent.getName()));
					logger.error("failed to attach uploaded files to sub asset", e);
				}
			}

			getSession().remove(SESSION_KEY);
			addFlashMessageText("message.mastereventsaved");
			return SUCCESS;

		} catch (ProcessingProofTestException e) {
			addActionError(getText("error.processingprooftest"));

			return INPUT;
		} catch (UnknownSubAsset e) {
			cleanToInspectionsToMatchConfiguration = true;
			addActionError(getText("error.assetconfigurationchanged"));
			return INPUT;
		} catch (FileAttachmentException e) {
			addActionError(getText("error.attachingfile"));
			return INPUT;
		} catch (Exception e) {
			addActionError(getText("error.eventsavefailed"));
			logger.error("event save failed serial number " + asset.getSerialNumber(), e);
			return ERROR;
		}
	}

	
	
	protected List<InspectionScheduleBundle> createInspectionScheduleBundles(List<WebInspectionSchedule> nextSchedules) {
		List<InspectionScheduleBundle> scheduleBundles = new ArrayList<InspectionScheduleBundle>();
		StrutsListHelper.clearNulls(nextSchedules);
		
		WebInspectionScheduleToInspectionScheduleBundleConverter converter = createWebInspectionScheduleToInspectionScheduleBundleConverter();
		
		for (WebInspectionSchedule nextSchedule : nextSchedules) {
			InspectionScheduleBundle bundle = converter.convert(nextSchedule, asset);
			scheduleBundles.add(bundle );
		}
	
		
		return scheduleBundles;
	}

	private WebInspectionScheduleToInspectionScheduleBundleConverter createWebInspectionScheduleToInspectionScheduleBundleConverter() {
		WebInspectionScheduleToInspectionScheduleBundleConverter converter = new WebInspectionScheduleToInspectionScheduleBundleConverter(getLoaderFactory(), getSessionUser().createUserDateConverter());
		return converter;
	}
	
	
	private void completeSchedule(Long inspectionScheduleId, EventSchedule eventSchedule) {
		if (inspectionScheduleId != null) {

			if (inspectionScheduleId.equals(InspectionScheduleSuggestion.NEW_SCHEDULE)) {
				eventSchedule = new EventSchedule(event);
			} else if (eventSchedule != null) {
				eventSchedule.completed(event);
			}
			if (eventSchedule != null) {
				try {
					inspectionScheduleManager.update(eventSchedule);
					addFlashMessageText("message.schedulecompleted");
				} catch (Exception e) {
					logger.error("could not complete the schedule", e);
					addFlashErrorText("error.completingschedule");
				}
			}
		}
	}

	public Long getAssetId() {
		return (asset != null) ? asset.getId() : null;
	}

	public void setAssetId(Long assetId) {
		if (assetId == null) {
			asset = null;

		} else if (asset == null || !asset.getId().equals(assetId)) {
			asset = persistenceManager.find(Asset.class, assetId, getSecurityFilter(), "type.subTypes");
			asset = new FindSubAssets(persistenceManager, asset).fillInSubAssets();
			if (asset != null) {
				for (SubAsset subAsset : asset.getSubAssets()) {
					persistenceManager.reattchAndFetch(subAsset.getAsset().getType(), "inspectionTypes");
				}
			}

		}
		
		if (masterInspection != null) {
			masterInspection.setMasterAsset(asset);
		}
	}

	public Long getInspectionGroupId() {
		return (eventGroup != null) ? eventGroup.getId() : null;
	}

	public void setInspectionGroupId(Long inspectionGroupId) {
		if (inspectionGroupId == null) {
			eventGroup = null;
		} else if (eventGroup == null || inspectionGroupId.equals(eventGroup.getId())) {
			eventGroup = persistenceManager.find(EventGroup.class, inspectionGroupId, getTenantId());
		}
	}

	public Long getType() {
		return (event != null && event.getType() != null) ? event.getType().getId() : null;
	}

	public void setType(Long type) {
		if (dirtySession) {
			event.setType(null);
		}
		if (type == null) {
			event.setType(null);
		} else if (event.getType() == null || !type.equals(event.getType())) {
			event.setType(persistenceManager.find(InspectionType.class, type, getTenantId()));
		}
	}

	public InspectionType getInspectionType() {
		return event.getType();
	}

	// validate this to be sure we have
	public MasterInspection getMasterInspection() {
		return masterInspection;
	}

	public List<SubEvent> getInspectionsFor(Asset asset) {
		return masterInspection.getSubInspectionFor(asset);
	}
	
	public String getNameFor(Asset asset) {
		List<SubEvent> subEvents = getInspectionsFor(asset);
		String result = null;
		if (!subEvents.isEmpty()) {
			result = subEvents.iterator().next().getName();
		}
		
		return result;
	}

	public Event getInspection() {
		return event;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isComplete() {
		return (masterInspection != null && masterInspection.isMainInspectionStored());
	}

	public Object getInspectionComplete() {
		if (isComplete()) {
			return new Object();
		}
		return null;
	}

	public boolean isCleanToInspectionsToMatchConfiguration() {
		return cleanToInspectionsToMatchConfiguration;
	}

	public void setCleanToInspectionsToMatchConfiguration(boolean cleanToInspectionsToMatchConfiguration) {
		this.cleanToInspectionsToMatchConfiguration = cleanToInspectionsToMatchConfiguration;
	}

	public boolean isMasterInspection(Long id) {
		return eventManager.isMasterEvent(id);
	}

	public List<Asset> getAvailableSubProducts() {
		List<Asset> availableSubAssets = new ArrayList<Asset>();
		for (SubEvent subEvent : event.getSubEvents()) {
			if (!availableSubAssets.contains(subEvent.getAsset())) {
				availableSubAssets.add(subEvent.getAsset());
			}
		}
		return availableSubAssets;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setSubAssets(List<SubAssetHelper> subAssets) {
		this.subAssets = subAssets;
	}

	public List<SubAssetHelper> getSubAssets() {
		if (subAssets == null) {
			subAssets = new ArrayList<SubAssetHelper>();
		}
		return subAssets;
	}

	public List<AssetType> getSubTypes() {
		return new ArrayList<AssetType>(asset.getType().getSubTypes());
	}

	public void setScheduleId(Long scheduleId) {
		if (masterInspection != null) {
			masterInspection.setScheduleId(scheduleId);
		}
	}
	
	public Long getScheduleId() {
		return (masterInspection != null) ? masterInspection.getScheduleId() : null;
	}
}
