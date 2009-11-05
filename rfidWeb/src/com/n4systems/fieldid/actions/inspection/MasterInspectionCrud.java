package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.InspectionScheduleSuggestion;
import com.n4systems.fieldid.actions.helpers.MasterInspection;
import com.n4systems.fieldid.actions.helpers.SubProductHelper;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.utils.CopyInspectionFactory;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubInspection;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.security.Permissions;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;


public class MasterInspectionCrud extends AbstractCrud {
	protected static final String SESSION_KEY = "masterInspection";
	private static Logger logger = Logger.getLogger(MasterInspectionCrud.class);
	private static final long serialVersionUID = 1L;

	private InspectionManager inspectionManager;
	private InspectionScheduleManager inspectionScheduleManager;

	private Inspection inspection;
	private InspectionGroup inspectionGroup;
	private Product product;
	private List<SubProductHelper> subProducts;

	private MasterInspection masterInspection;
	private String token;
	private boolean dirtySession = true;

	private boolean cleanToInspectionsToMatchConfiguration = false;

	public MasterInspectionCrud(PersistenceManager persistenceManager, InspectionManager inspectionManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.inspectionManager = inspectionManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
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
		masterInspection.getInspection().setProduct(product);
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
					persistenceManager.reattchAndFetch(i, "product.id", "results", "infoOptionMap", "type", "attachments");
				}
			}
		}

		if (masterInspection != null) {
			inspection = masterInspection.getInspection();
			token = masterInspection.getToken();
			setProductId(masterInspection.getInspection().getProduct().getId());
			getSession().put("masterInspection", masterInspection);
		}
	}

	@SkipValidation
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
	public String doAdd() {

		if (masterInspection == null) {
			addActionError(getText("error.nomasterinspection"));
			return MISSING;
		}

		if (product == null) {
			if (masterInspection.getInspection().getProduct() == null) {
				addActionError(getText("error.noproduct"));
				return MISSING;
			} else {
				product = masterInspection.getInspection().getProduct();
			}
		}

		if (masterInspection.getInspection() == null) {
			addActionError(getText("error.noinspection"));
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
			addActionError(getText("error.nomasterinspection"));
			return MISSING;
		}

		if (product == null) {
			if (masterInspection.getInspection().getProduct() == null) {
				addActionError(getText("error.noproduct"));
				return MISSING;
			} else {
				product = masterInspection.getInspection().getProduct();
			}
		}

		if (masterInspection.getInspection() == null) {
			addActionError(getText("error.noinspection"));
			return MISSING;
		}

		return SUCCESS;
	}

	@Validations(requiredFields = { @RequiredFieldValidator(message = "", key = "error.masterinspectionnotcomplete", fieldName = "inspectionComplete") })
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection, Permissions.EditInspection})
	public String doSave() {

		if (masterInspection == null) {
			return ERROR;
		}

		setInspectionGroupId(masterInspection.getInspectionGroupId());
		inspection.setGroup(inspectionGroup);

		try {
			if (uniqueID == null) {
				if (cleanToInspectionsToMatchConfiguration) {
					masterInspection.cleanSubInspectionsForNonValidSubProducts(product);
				}
				Inspection master = CopyInspectionFactory.copyInspection(masterInspection.getCompletedInspection());
				inspection = inspectionManager.createInspection(master, masterInspection.getProductStatus(), masterInspection.getNextDate(), getSessionUser().getUniqueID(), masterInspection
						.getProofTestFile(), masterInspection.getUploadedFiles());
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
					addFlashError(getText("error.subinspectionfileupload", subInspection.getName()));
					logger.error("failed to attach uploaded files to sub product", e);
				}
			}

			getSession().remove(SESSION_KEY);
			addFlashMessage(getText("message.masterinspectionsaved"));
			return SUCCESS;

		} catch (ProcessingProofTestException e) {
			addActionError(getText("error.processingprooftest"));

			return INPUT;
		} catch (UnknownSubProduct e) {
			cleanToInspectionsToMatchConfiguration = true;
			addActionError(getText("error.productconfigurationchanged"));
			return INPUT;
		} catch (FileAttachmentException e) {
			addActionError(getText("error.attachingfile"));
			return INPUT;
		} catch (Exception e) {
			addActionError(getText("error.inspectionsavefailed"));
			logger.error("inspection save failed serial number " + product.getSerialNumber(), e);
			return ERROR;
		}
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

	public Long getProductId() {
		return (product != null) ? product.getId() : null;
	}

	public void setProductId(Long productId) {		
		if (productId == null) {
			product = null;

		} else if (product == null || !product.getId().equals(productId)) {
			product = persistenceManager.find(Product.class, productId, getSecurityFilter(), "type.subTypes");
			product = new FindSubProducts(persistenceManager, product).fillInSubProducts();
			if (product != null) {
				for (com.n4systems.model.SubProduct subProduct : product.getSubProducts()) {
					persistenceManager.reattchAndFetch(subProduct.getProduct().getType(), "inspectionTypes");
				}
			}

		}
		
		if (masterInspection != null) {
			masterInspection.setMasterProduct(product);
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

	public List<SubInspection> getInspectionsFor(Product product) {
		return masterInspection.getSubInspectionFor(product);
	}
	
	public String getNameFor(Product product) {
		List<SubInspection> subInspections = getInspectionsFor(product);
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

	public List<Product> getAvailableSubProducts() {
		List<Product> availableSubProducts = new ArrayList<Product>(); 
		for (SubInspection subInspection : inspection.getSubInspections()) {
			if (!availableSubProducts.contains(subInspection.getProduct())) {
				availableSubProducts.add(subInspection.getProduct());
			}
		}
		return availableSubProducts;
	}

	public Product getProduct() {
		return product;
	}

	public void setSubProducts(List<SubProductHelper> subProducts) {
		this.subProducts = subProducts;
	}

	public List<SubProductHelper> getSubProducts() {
		if (subProducts == null) {
			subProducts = new ArrayList<SubProductHelper>();
		}
		return subProducts;
	}

	public List<ProductType> getSubTypes() {
		return new ArrayList<ProductType>(product.getType().getSubTypes());
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
