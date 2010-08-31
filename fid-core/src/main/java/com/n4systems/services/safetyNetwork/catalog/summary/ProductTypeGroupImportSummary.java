package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.ProductTypeGroup;

public class ProductTypeGroupImportSummary extends BaseImportSummary {

	private List<ProductTypeGroup> importedProductTypeGroupNames = new ArrayList<ProductTypeGroup>();
	private Map<Long, ProductTypeGroup> importMapping = new HashMap<Long, ProductTypeGroup>();
	
	private List<ProductTypeGroup> createdGroups = new ArrayList<ProductTypeGroup>();

	public int getNumberOfGroupsToBeImported() {
		return importedProductTypeGroupNames.size();
	}

	public List<ProductTypeGroup> getImportedProductTypeGroupNames() {
		return importedProductTypeGroupNames;
	}

	public Map<Long, ProductTypeGroup> getImportMapping() {
		return importMapping;
	}

	public List<ProductTypeGroup> getCreatedGroups() {
		return createdGroups;
	}
	
	public void createdGroup(ProductTypeGroup group) {
		createdGroups.add(group);
	}
}
