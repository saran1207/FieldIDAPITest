package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MultiInspectActionHelper;
import com.n4systems.fieldid.actions.inspection.viewmodel.InspectionWebModel;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.collection.helpers.CommonAssetValues;
import com.n4systems.fieldid.collection.helpers.CommonAssetValuesFinder;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.handlers.CommonInspectionTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonInspectionTypeHandler;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.api.Listable;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
public class MultiInspectAction extends AbstractCrud {

	private static final long UNASSIGNED_OPTION_VALUE = 0L;
	private static final long KEEP_THE_SAME_OPTION = -1L;
	private List<Long> assetIds = new ArrayList<Long>();
	private Set<InspectionType> eventTypes;

	private InspectionType inspectionType;
	private Inspection inspection;
	
	private final InspectionFormHelper inspectionFormHelper;
	
	
	
	private List<Product> assets;

	private UserManager userManager;
	private List<ListingPair> examiners;
	private List<Listable<Long>> commentTemplates;
	private List<ProductStatusBean> productStatuses;
	private CommonInspectionTypeHandler commonInspectionTypeHandler;
	private CommonAssetValues commonAssetValues;
	private InspectionWebModel modifiableInspection;
	private MultiInspectGroupSorter multiInspectGroupSorter;
	private List<Listable<Long>> employees;
	

	public MultiInspectAction(PersistenceManager persistenceManager, UserManager userManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.inspectionFormHelper = new InspectionFormHelper();
	}
	

	@Override
	protected void initMemberFields() {
		inspection = new Inspection();
		inspection.setType(inspectionType);
		inspection.setDate(new Date());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		initMemberFields();
	}
	
	@Override
	protected void postInit() {
		super.postInit();
		commonInspectionTypeHandler = createCommonInspectionTypeHandler();
		modifiableInspection = new InspectionWebModel(new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), inspection), getSessionUser().createUserDateConverter(), this);
		overrideHelper(new MultiInspectActionHelper(getLoaderFactory()));
	}
	
	public void testDependancies() {
		if (getAssets() == null || getAssets().isEmpty()) {
			addActionErrorText("error.no_assets_given");
			throw new MissingEntityException("no assets given");
		}
	}
	
	
	@SkipValidation
	public String doInspectionTypes() {
		testDependancies();
		return SUCCESS;
	}

	private CommonInspectionTypeHandler createCommonInspectionTypeHandler() {
		return new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeDatabaseLoader(getSecurityFilter(), ConfigContext.getCurrentContext()));
	}

	@SkipValidation
	public String doPerformEvent() {
		testDependancies();
		
		commonAssetValues = new CommonAssetValuesFinder(getAssets()).findCommonValues();
		inspection.setOwner(commonAssetValues.owner);
		if (commonAssetValues.hasCommonLocation()) {
			inspection.setAdvancedLocation(commonAssetValues.location);
		}
		modifiableInspection.updateValuesToMatch(inspection);
		
		return SUCCESS;
		
	}
	
	
	public String doInspectionCheck() {
		return SUCCESS;
	}
	

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public Set<InspectionType> getEventTypes() {
		if (eventTypes == null) {
			eventTypes = commonInspectionTypeHandler.findCommonInspectionTypesFor(assetIds);
		}
		return eventTypes;
	}


	public Long getType() {
		return inspectionType != null ? inspectionType.getId() : null;
	}

	public void setType(Long type) {
		if (type == null) {
			inspectionType = null;
		} else if (inspectionType == null || !type.equals(inspectionType.getId())) {
			inspectionType = persistenceManager.find(InspectionType.class, type, getTenantId(), "sections", "supportedProofTests", "infoFieldNames");
		}
	}

	public Long getPerformedBy() {
		return getSessionUser().getUniqueID();
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public List<Product> getAssets() {
		if (assets == null) {
			if (!assetIds.isEmpty()) {
				assets = persistenceManager.findAll(new QueryBuilder<Product>(Product.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", assetIds));
			} else {
				assets = new ArrayList<Product>();
			}
		}
				
		return assets;
	}

	public boolean isPrintable() {
		return inspectionType.isPrintable();
	}

	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getExaminers(getSecurityFilter());
		}
		return examiners;
	}

	public Inspection getInspection() {
		return inspection;
	}

	
	public void setInspection(Inspection inspection) {
		this.inspection = inspection;
	}
	

	public InspectionFormHelper getInspectionFormHelper() {
		return inspectionFormHelper;
	}
	
	
	public List<Listable<Long>> getCommentTemplates() {
		if (commentTemplates == null) {
			commentTemplates = getLoaderFactory().createCommentTemplateListableLoader().load();
		}
		return commentTemplates;
	}


	public List<ProductStatusBean> getProductStatuses() {
		if (productStatuses == null) {
			productStatuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		return productStatuses;
	}
	
	public Long getAssignedToId() {
		if (commonAssetValues.hasCommonAssignment()) { 
			return commonAssetValues.assignment.assignTo != null ? commonAssetValues.assignment.assignTo.getId() : UNASSIGNED_OPTION_VALUE; 
		}
		
		return KEEP_THE_SAME_OPTION;
	}
	
	public Long getProductStatus() {
		return commonAssetValues.productStatus != null ? commonAssetValues.productStatus.getUniqueID() : null;
	}
	

	@VisitorFieldValidator(message="")
	public InspectionWebModel getModifiableInspection() {
		if (modifiableInspection == null) {
			throw new NullPointerException("action has not been initialized.");
		}
		return modifiableInspection;
	}
	
	public MultiInspectGroupSorter getMultiInspectGroupSorter() {
		if (multiInspectGroupSorter == null) {
			multiInspectGroupSorter = new MultiInspectGroupSorter(getEventTypes());
		}
		return multiInspectGroupSorter;
	}
	
	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(0L, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createCurrentEmployeesListableLoader().load());
		}
		return employees;
	}
	
	public List<WebInspectionSchedule> getNextSchedules() {
		return new ArrayList<WebInspectionSchedule>();
	}
}
