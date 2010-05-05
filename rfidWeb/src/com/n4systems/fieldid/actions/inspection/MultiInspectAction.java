package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.User;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.handlers.CommonInspectionTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonInspectionTypeHandler;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class MultiInspectAction extends AbstractAction {

	private List<Long> assetIds = new ArrayList<Long>();
	private Set<InspectionType> eventTypes;
	private List<ProductType> findCommonEventTypes;

	private InspectionType inspectionType;
	private Inspection inspection;
	
	private final InspectionFormHelper inspectionFormHelper;
	private List<Product> assets;

	private User userManager;
	private List<ListingPair> inspectors;

	public MultiInspectAction(PersistenceManager persistenceManager, User userManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.inspectionFormHelper = new InspectionFormHelper();
	}

	public String doInspectionTypes() {
		CommonInspectionTypeHandler loaderBackedCommonInspectionTypeHandler = createCommonInspectionTypeHandler();

		eventTypes = loaderBackedCommonInspectionTypeHandler.findCommonInspectionTypesFor(assetIds);

		return SUCCESS;
	}

	private CommonInspectionTypeHandler createCommonInspectionTypeHandler() {
		return new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeDatabaseLoader(getSecurityFilter(), ConfigContext.getCurrentContext()));
	}

	public String doPerformEvent() {
		assets = persistenceManager.findAll(new QueryBuilder<Product>(Product.class, getSecurityFilter()).addWhere(Comparator.IN, "assetIds", "id", assetIds));

		inspection = new Inspection();
		inspection.setType(inspectionType);
		return SUCCESS;
	}

	public List<Long> getAssetIds() {
		return assetIds;
	}

	public Set<InspectionType> getEventTypes() {
		return eventTypes;
	}

	public List<ProductType> getFindCommonEventTypes() {
		return findCommonEventTypes;
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

	public String getInspectionDate() {
		return convertDateTime(DateHelper.getTodayWithTime());
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

}
