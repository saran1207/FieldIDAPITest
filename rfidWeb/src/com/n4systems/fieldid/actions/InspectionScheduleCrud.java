package com.n4systems.fieldid.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.model.safetynetwork.HasLinkedProductsLoader;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.security.Permissions;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
public class InspectionScheduleCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InspectionScheduleCrud.class);

	private LegacyProductSerial legacyProductManager;
	private InspectionScheduleManager inspectionScheduleManager;
	private InspectionSchedule inspectionSchedule;
	
	private InspectionType inspectionType;
	private List<ListingPair> jobs;
	private Product product;
	private String nextDate;

	private List<InspectionSchedule> inspectionSchedules;

	public InspectionScheduleCrud(LegacyProductSerial legacyProductManager, PersistenceManager persistenceManager, InspectionScheduleManager inspectionScheduleManager) {
		super(persistenceManager);
		this.legacyProductManager = legacyProductManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
	}

	@Override
	protected void initMemberFields() {
		inspectionSchedule = new InspectionSchedule();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		inspectionSchedule = persistenceManager.find(InspectionSchedule.class, uniqueId, getTenantId());
	}

	private void testRequiredEntities(boolean existing) {
		testRequiredEntities(existing, false);
	}
	private void testRequiredEntities(boolean existing, boolean inspectionTypeRequired) {
		if (inspectionSchedule == null) {
			addActionErrorText("error.noschedule");
			throw new MissingEntityException();
		} else if (existing && inspectionSchedule.isNew()) {
			addActionErrorText("error.noschedule");
			throw new MissingEntityException();
		}
		
		if (product == null) {
			addActionErrorText("error.noproduct");
			throw new MissingEntityException();
		}
		
		if (inspectionTypeRequired && inspectionType == null) {
			addActionErrorText("error.noinspectiontype");
			throw new MissingEntityException();
		} 
	}
	
	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return INPUT;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	
	public String doCreate() {
		testRequiredEntities(false);
		try {
			Project tmpProject = inspectionSchedule.getProject();
			inspectionSchedule = new InspectionSchedule(product, inspectionType);
			inspectionSchedule.setNextDate(convertDate(nextDate));
			inspectionSchedule.setProject(tmpProject);
			
			uniqueID = new InspectionScheduleServiceImpl(persistenceManager).createSchedule(inspectionSchedule);
			addActionMessageText("message.inspectionschedulesaved");
		} catch (Exception e) {
			logger.error("could not save schedule", e);
			addActionErrorText("error.savinginspectionschedule");
			return ERROR;
			
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doShow() {
		testRequiredEntities(true);

		return SUCCESS;
	}

	
	
	@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection, Permissions.ManageJobs})
	public String doSave() {
		testRequiredEntities(true);
		try {
			inspectionSchedule.setNextDate(convertDate(nextDate));
			new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(inspectionSchedule);
			addActionMessageText("message.inspectionschedulesaved");
		} catch (Exception e) {
			logger.error("could not save schedule", e);
			addActionErrorText("error.savinginspectionschedule");
			return ERROR;
			
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		testRequiredEntities(false, false);
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		try {
			persistenceManager.delete(inspectionSchedule);
			addActionMessageText("message.inspectionscheduledeleted");
		} catch (Exception e) {
			logger.error("could not delete schedule", e);
			addActionErrorText("error.deletinginspectionschedule");
			return ERROR;
		}

		return SUCCESS;
	}
	
	@SkipValidation
	public String doStopProgress() {
		testRequiredEntities(true, false);
		try {
			inspectionSchedule.stopProgress();
			persistenceManager.update(inspectionSchedule, getSessionUser().getId());
			addActionMessageText("message.inspectionscheduleprogressstoped");
		} catch (Exception e) {
			logger.error("could not stop progress on the schedule", e);
			addActionErrorText("error.stopingprogressinspectionschedule");
			return ERROR;
		}

		return SUCCESS;
	}

	public Long getProductId() {
		return (product != null) ? product.getId() : null;
	}

	public Product getProduct() {
		return product;
	}

	public void setProductId(Long product) {
		if (this.product == null || !this.product.getId().equals(product)) {
			this.product = persistenceManager.find(Product.class, product, getSecurityFilter(), "type.subTypes", "type.inspectionTypes");
			this.product = new FindSubProducts(persistenceManager, this.product).fillInSubProducts();
		}
	}

	public Long getType() {
		return (inspectionType != null) ? inspectionType.getId() : null;
	}

	public void setType(Long inspectionTypeId) {
		if (this.inspectionType == null || !this.inspectionType.getId().equals(inspectionTypeId)) {
			this.inspectionType = null;
			for (InspectionType insType : getInspectionTypes()) {
				if (insType.getId().equals(inspectionTypeId)) {
					this.inspectionType = insType;
					break;
				}
			}
		}
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public List<InspectionType> getInspectionTypes() {
		List<InspectionType> inspectionTypes = new ArrayList<InspectionType>();
		List<AssociatedInspectionType> associatedInspectionTypes = getLoaderFactory().createAssociatedInspectionTypesLoader().setProductType(product.getType()).load();
		for (AssociatedInspectionType associatedInspectionType : associatedInspectionTypes) {
			inspectionTypes.add(associatedInspectionType.getInspectionType());
		}
		return inspectionTypes;
		
	}

	public String getNextDate() {
		if (nextDate == null) {
			nextDate = convertDate(inspectionSchedule.getNextDate());
		}
		return nextDate;
	}

	
	@RequiredStringValidator(type = ValidatorType.SIMPLE, fieldName = "nextDate", message = "", key = "error.mustbeadate")
	@CustomValidator(type = "n4systemsDateValidator", fieldName = "nextDate", message = "", key = "error.mustbeadate")
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public List<InspectionSchedule> getInspectionSchedules() {
		if (inspectionSchedules == null) {
			inspectionSchedules = inspectionScheduleManager.getAvailableSchedulesFor(product);
		}
		return inspectionSchedules;
	}

	public Long getInspectionCount() {
		return legacyProductManager.countAllInspections(product, getSecurityFilter());
	}

	public InspectionSchedule getInspectionSchedule() {
		return inspectionSchedule;
	}

	public List<ListingPair> getJobs() {
		if (jobs == null) {
			QueryBuilder<ListingPair> query = new QueryBuilder<ListingPair>(Project.class, getSecurityFilter());
			query.addSimpleWhere("eventJob", true);
			query.addSimpleWhere("retired", false);
			jobs = persistenceManager.findAllLP(query, "name");
		}
		return jobs;
	}

	public Long getProject() {
		return (inspectionSchedule.getProject() != null) ? inspectionSchedule.getProject().getId() : null;
	}

	public void setProject(Long project) {
		if (project == null) {
			inspectionSchedule.setProject(null);
		} else if (inspectionSchedule.getProject() == null || !project.equals(inspectionSchedule.getProduct().getId())) {
			inspectionSchedule.setProject(persistenceManager.find(Project.class, project, getTenantId()));
		}
	}

	public boolean isLinked() {
		if (product == null) {
			return false;
		} else if (product.isLinked()) {
			return true;
		}
		
		// this checks if there are any products linked to this product
		HasLinkedProductsLoader hasLinkedLoader = getLoaderFactory().createHasLinkedProductsLoader();
		hasLinkedLoader.setNetworkId(product.getNetworkId());
		hasLinkedLoader.setProductId(product.getId());
		
		boolean hasLinked = hasLinkedLoader.load();
		return hasLinked;
	}
}
