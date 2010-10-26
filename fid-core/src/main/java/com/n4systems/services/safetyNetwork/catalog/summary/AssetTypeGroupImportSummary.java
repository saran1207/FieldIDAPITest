package com.n4systems.services.safetyNetwork.catalog.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AssetTypeGroup;

public class AssetTypeGroupImportSummary extends BaseImportSummary {

	private List<AssetTypeGroup> importedAssetTypeGroupNames = new ArrayList<AssetTypeGroup>();
	private Map<Long, AssetTypeGroup> importMapping = new HashMap<Long, AssetTypeGroup>();
	
	private List<AssetTypeGroup> createdGroups = new ArrayList<AssetTypeGroup>();

	public int getNumberOfGroupsToBeImported() {
		return importedAssetTypeGroupNames.size();
	}

	public List<AssetTypeGroup> getImportedProductTypeGroupNames() {
		return importedAssetTypeGroupNames;
	}

	public Map<Long, AssetTypeGroup> getImportMapping() {
		return importMapping;
	}

	public List<AssetTypeGroup> getCreatedGroups() {
		return createdGroups;
	}
	
	public void createdGroup(AssetTypeGroup group) {
		createdGroups.add(group);
	}
}
