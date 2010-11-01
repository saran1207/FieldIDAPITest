package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.fieldid.actions.helpers.SubAssetHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.SubAsset;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionManager;
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
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.SubInspection;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


public class MasterInspectionCrud extends AbstractCrud {
	protected static final String SESSION_KEY = "masterInspection";
	private static Logger logger = Logger.getLogger(MasterInspectionCrud.class);
	private static final long serialVersionUID = 1L;

	private InspectionManager inspectionManager;
	private InspectionScheduleManager inspectionScheduleManager;
	private final InspectionPersistenceFactory inspectionPersistenceFactory;

	private Inspection inspection;
	private InspectionGroup inspectionGroup;
	private Asset asset;
	private List<SubAssetHelper> subAssets;

	private MasterInspection masterInspection;
	private String token;
	private boolean dirtySession = true;

	private boolean cleanToInspectionsToMatchConfiguration = false;
	

	public MasterInspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
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

		if (inspectionGroup == null) {
			setInspectionGroupId(masterInspection.getInspectionGroupId());
		}
		inspection = masterInspection.getInspection();
	}

	private void createNewMasterInspection() {
		masterInspection = new MasterInspection();
		masterInspection.setInspection(new Inspection());
		token = masterInspection.getToken();
		masterInspection.getInspection().setAsset(asset);
		getSession().put("masterInspection", masterInspection);
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		masterInspection = (MasterInspection) getSession().get(SESSION_KEY);

		if (masterInspection == null || token == null || !MasterInspection.matchingMasterInspection(masterInspection, token)) {
			Inspection inspection = inspectionManager.findAllFields(uniqueId, getSecurityFilter());
			masterInspection = new MasterInspection(inspection);
			if (inspection != null) {
				for (SubInspection i : inspection.getSubInspections()) {
					persistenceManager.reattchAndFetch(i, "asset.id", "results", "infoOptionMap", "type", "attachments");
				}
			}
		}

		if (masterInspection != null) {
			inspection = masterInspection.getInspection();
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

		inspection = masterInspection.getInspection();

		if (inspectionGroup != null) {
			masterInspection.setInspectionGroupId(inspectionGroup.getId());
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
		inspection.setGroup(inspectionGroup);

		try {
			if (uniqueID == null) {
				if (cleanToInspectionsToMatchConfiguration) {
					masterInspection.cleanSubInspectionsForNonValidSubAssets(asset);
				}
				Inspection master = CopyInspectionFactory.copyInspection(masterInspection.getCompletedInspection());
				
				
				CreateInspectionParameterBuilder createInspecitonBuiler = new CreateInspectionParameterBuilder(master, getSessionUserId())
						.withProofTestFile(masterInspection.getProofTestFile())
						.withUploadedImages(masterInspection.getUploadedFiles());
				
				
				
				createInspecitonBuiler.addSchedules(createInspectionScheduleBundles(masterInspection.getNextSchedules()));
				
				inspection = inspectionPersistenceFactory.createInspectionCreator().create(
						createInspecitonBuiler.build());
				uniqueID = inspection.getId();
			} else {
				Inspection master = CopyInspectionFactory.copyInspection(masterInspection.getCompletedInspection());
				inspection = inspectionManager.updateInspection(master, getSessionUser().getUniqueID(), masterInspection.getProofTestFile(), masterInspection.getUploadedFiles());
			}

			completeSchedule(masterInspection.getScheduleId(), masterInspection.getSchedule());

			for (int i = 0; i < masterInspection.getSubInspections().size(); i++) {
				SubInspection subInspection = new SubInspection();
				subInspection.setName("unknown");
				SubInspection uploadedFileKey = masterInspection.getSubInspections().get(i);
				try {
					subInspection = inspection.getSubInspections().get(i);

					inspection = inspectionManager.attachFilesToSubInspection(inspection, subInspection, masterInspection.getSubInspectionUploadedFiles().get(uploadedFileKey));

				} catch (Exception e) {
					addFlashError(getText("error.subeventfileupload", subInspection.getName()));
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
	
	
	private void completeSchedule(Long inspectionScheduleId, InspectionSchedule inspectionSchedule) {
		if (inspectionScheduleId != null) {

			if (inspectionScheduleId.equals(InspectionScheduleSuggestion.NEW_SCHEDULE)) {
				inspectionSchedule = new InspectionSchedule(inspection);
			} else if (inspectionSchedule != null) {
				inspectionSchedule.completed(inspection);
			}
			if (inspectionSchedule != null) {
				try {
					inspectionScheduleManager.update(inspectionSchedule);
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
		return (inspectionGroup != null) ? inspectionGroup.getId() : null;
	}

	public void setInspectionGroupId(Long inspectionGroupId) {
		if (inspectionGroupId == null) {
			inspectionGroup = null;
		} else if (inspectionGroup == null || inspectionGroupId.equals(inspectionGroup.getId())) {
			inspectionGroup = persistenceManager.find(InspectionGroup.class, inspectionGroupId, getTenantId());
		}
	}

	public Long getType() {
		return (inspection != null && inspection.getType() != null) ? inspection.getType().getId() : null;
	}

	public void setType(Long type) {
		if (dirtySession) {
			inspection.setType(null);
		}
		if (type == null) {
			inspection.setType(null);
		} else if (inspection.getType() == null || !type.equals(inspection.getType())) {
			inspection.setType(persistenceManager.find(InspectionType.class, type, getTenantId()));
		}
	}

	public InspectionType getInspectionType() {
		return inspection.getType();
	}

	// validate this to be sure we have
	public MasterInspection getMasterInspection() {
		return masterInspection;
	}

	public List<SubInspection> getInspectionsFor(Asset asset) {
		return masterInspection.getSubInspectionFor(asset);
	}
	
	public String getNameFor(Asset asset) {
		List<SubInspection> subInspections = getInspectionsFor(asset);
		String result = null;
		if (!subInspections.isEmpty()) {
			result = subInspections.iterator().next().getName();
		}
		
		return result;
	}

	public Inspection getInspection() {
		return inspection;
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
		return inspectionManager.isMasterInspection(id);
	}

	public List<Asset> getAvailableSubProducts() {
		List<Asset> availableSubAssets = new ArrayList<Asset>();
		for (SubInspection subInspection : inspection.getSubInspections()) {
			if (!availableSubAssets.contains(subInspection.getAsset())) {
				availableSubAssets.add(subInspection.getAsset());
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
