package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.handlers.LoaderBackedCommonInspectionTypeHandler;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.inspectiontype.CommonProductTypeDatabaseLoader;

public class MultiInspectAction extends AbstractAction {

	
	private List<Long> assetIds = new ArrayList<Long>();
	private Set<InspectionType> eventTypes;
	private List<ProductType> findCommonEventTypes;
	
	public MultiInspectAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	
	public String doInspectionTypes() {
		eventTypes = new LoaderBackedCommonInspectionTypeHandler(new CommonProductTypeDatabaseLoader(getSecurityFilter())).findCommonInspectionTypesFor(assetIds);

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

}
