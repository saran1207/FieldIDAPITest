package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetTypeLister {
	private PersistenceManager persistenceManager;
	private SecurityFilter filter;
	private List<ListingPair> assetTypes;
	private List<String> orderedAssetTypeGroups;
	private List<ListingPair> assetTypeGroups;
	private Map<String,List<ListingPair>> groupedAssetTypes;
	
	private List<AssetTypeGroup> groups;

	private Map<Long,List<ListingPair>> groupedAssetTypesById;
	
	public AssetTypeLister(PersistenceManager persistenceManager, SecurityFilter filter) {
		this.persistenceManager = persistenceManager;
		this.filter = filter;
	}

	public List<String> getGroups() {
		if (orderedAssetTypeGroups == null) {
			orderedAssetTypeGroups = new ArrayList<String>();
			for (AssetTypeGroup assetTypeGroup : getPTGroups()) {
				orderedAssetTypeGroups.add(assetTypeGroup.getDisplayName());
			}
		}
		return orderedAssetTypeGroups;
	}
	
	private List<AssetTypeGroup> getPTGroups() {
		if (groups == null) {
			QueryBuilder<AssetTypeGroup> query = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, filter).addOrder("orderIdx");
			groups = persistenceManager.findAll(query);
		}
		return groups;
	}
	
	public List<ListingPair> getAssetTypeGroups() {
		if (assetTypeGroups == null) {
			assetTypeGroups = com.n4systems.util.ListHelper.longListableToListingPair(getPTGroups());
		}
		return assetTypeGroups;
	}

	public List<ListingPair> getGroupedAssetTypes(String group) {
		if (groupedAssetTypes == null) {
			groupedAssetTypes = new HashMap<String, List<ListingPair>>();
			
			for (String groupName : getGroups()) {
				List<ListingPair> types = findAssetTypesForGroup(groupName);
				assignGroupToList(groupName, types);
			}
		}
		
		List<ListingPair> list = groupedAssetTypes.get(group);
		return list != null ? list : new ArrayList<ListingPair>();
	}
	
	public List<ListingPair> getGroupedAssetTypesById(Long groupId) {
		if (groupId == null) {
			groupId = -1L;
		}
		if (groupedAssetTypesById == null) {
			groupedAssetTypesById = new HashMap<Long, List<ListingPair>>();
			
			
			for (ListingPair group : getAssetTypeGroups()) {
				List<ListingPair> types = getGroupedAssetTypes(group.getName());
				groupedAssetTypesById.put(group.getId(), types);
			}
			groupedAssetTypesById.put(-1L, getAssetTypes());
		}
		
		List<ListingPair> list = groupedAssetTypesById.get(groupId);
		return list;
	}

	public List<ListingPair> getAssetTypes() {
		if (assetTypes == null) {
			assetTypes = persistenceManager.findAllLP(AssetType.class, filter, "name");
		}
		return assetTypes;
	}
	
	private void assignGroupToList(String groupName, List<ListingPair> types) {
		if (!types.isEmpty()) {
			Collections.sort(types);
			groupedAssetTypes.put(groupName, types);
		}
	}

	private List<ListingPair> findAssetTypesForGroup(String groupName) {
		List<ListingPair> types = new ArrayList<ListingPair>();
		
		for (ListingPair type : persistenceManager.findAllLP(AssetType.class, filter, "group.name")) {
			if (groupName.equals(type.getName())) {
				types.add(getAssetTypeListingPairForId(type.getId()));
			}
		}
		
		return types;
	}

	private ListingPair getAssetTypeListingPairForId(Long assetTypeId) {
		ListingPair tmpListingToFind = new ListingPair(assetTypeId, "no_name");
		
		int indexOfRealListingPair = getAssetTypes().indexOf(tmpListingToFind);
		return getAssetTypes().get(indexOfRealListingPair);
	}

	
}
