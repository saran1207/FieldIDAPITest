package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.User;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.inspection.viewmodel.WebModifiedableInspection;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.collection.helpers.CommonAssetValues;
import com.n4systems.fieldid.collection.helpers.CommonAssetValuesFinder;
import com.n4systems.handlers.CommonInspectionTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonInspectionTypeHandler;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.api.Listable;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public class MultiInspectAction extends AbstractCrud {

	private List<Long> assetIds = new ArrayList<Long>();
	private Set<InspectionType> eventTypes;

	private InspectionType inspectionType;
	private Inspection inspection;
	
	private final InspectionFormHelper inspectionFormHelper;
	
	
	
	private List<Product> assets;

	private User userManager;
	private List<ListingPair> inspectors;
	private List<Listable<Long>> commentTemplates;
	private List<ProductStatusBean> productStatuses;
	private CommonInspectionTypeHandler commonInspectionTypeHandler;
	private CommonAssetValues commonAssetValues;
	private WebModifiedableInspection modifiableInspection;
	

	public MultiInspectAction(PersistenceManager persistenceManager, User userManager) {
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
		modifiableInspection = new WebModifiedableInspection(new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), inspection), getSessionUser().createUserDateConverter());
	}
	
	@SkipValidation
	public String doInspectionTypes() {
		return SUCCESS;
	}

	private CommonInspectionTypeHandler createCommonInspectionTypeHandler() {
		return new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeDatabaseLoader(getSecurityFilter(), ConfigContext.getCurrentContext()));
	}

	@SkipValidation
	public String doPerformEvent() {
		assets = persistenceManager.findAll(new QueryBuilder<Product>(Product.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", assetIds));
		
		commonAssetValues = new CommonAssetValuesFinder(assets).findCommonValues();
		inspection.setOwner(commonAssetValues.owner);
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


	public Long getEventTypeId() {
		return inspectionType != null ? inspectionType.getId() : null;
	}

	public void setEventTypeId(Long type) {
		if (type == null) {
			inspectionType = null;
		} else if (inspectionType == null || !type.equals(inspectionType.getId())) {
			inspectionType = persistenceManager.find(InspectionType.class, type, getTenantId(), "sections", "supportedProofTests", "infoFieldNames");
		}
	}


	public Long getInspector() {
		return getSessionUser().getUniqueID();
	}

	public InspectionType getInspectionType() {
		return inspectionType;
	}

	public List<Product> getAssets() {
		return assets;
	}

	public boolean isPrintable() {
		return inspectionType.isPrintable();
	}

	public List<ListingPair> getInspectors() {
		if (inspectors == null) {
			inspectors = userManager.getInspectorList(getSecurityFilter());
		}
		return inspectors;
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
	
	
	public BaseOrg getOwner() {
		return commonAssetValues.owner;
	}
	
	public Long getProductStatus() {
		return commonAssetValues.productStatus != null ? commonAssetValues.productStatus.getUniqueID() : null;
	}
	
	public String getLocation() {
		return commonAssetValues.location;
	}


	@VisitorFieldValidator(message="")
	public WebModifiedableInspection getModifiableInspection() {
		return modifiableInspection;
	}
}
