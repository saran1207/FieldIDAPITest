package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;
import com.n4systems.model.inspectiontype.CommonProductTypeLoader;

public class MultiInspectAction extends AbstractAction {

	
	private List<Long> assetIds = new ArrayList<Long>();
	private List<InspectionType> eventTypes;
	private List<ProductType> findCommonEventTypes;
	
	public MultiInspectAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	public String doInspectionTypes() {
		eventTypes = null;
		findCommonEventTypes = findCommonEventTypes();
		
		return SUCCESS;
	}


	private List<ProductType> findCommonEventTypes() {
		CommonProductTypeLoader commonEventTypeLoader = new CommonProductTypeDatabaseLoader(getSecurityFilter());
		return commonEventTypeLoader.forAssets(assetIds).load();
	}

	
	
	public List<Long> getAssetIds() {
		return assetIds;
	}


	public List<InspectionType> getEventTypes() {
		return eventTypes;
	}


	public List<ProductType> getFindCommonEventTypes() {
		return findCommonEventTypes;
	}

}
