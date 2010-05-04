package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.handlers.CommonInspectionTypeHandler;
import com.n4systems.handlers.LoaderBackedCommonInspectionTypeHandler;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class MultiInspectAction extends AbstractAction {

	
	private List<Long> assetIds = new ArrayList<Long>();
	private Set<InspectionType> eventTypes;
	private List<ProductType> findCommonEventTypes;
	
	private InspectionType inspectionType;
	
	
	private List<Product> assets;
	
	public MultiInspectAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
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
			inspectionType = persistenceManager.find(InspectionType.class, type, getTenantId());
		}
	}


	public InspectionType getInspectionType() {
		return inspectionType;
	}


	public List<Product> getAssets() {
		
		return assets;
	}

}
