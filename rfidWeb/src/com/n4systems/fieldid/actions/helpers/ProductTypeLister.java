package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductTypeLister {
	private PersistenceManager persistenceManager;
	private SecurityFilter filter;
	private List<ListingPair> productTypes;
	private List<String> orderedProductTypeGroups;
	private Map<String,List<ListingPair>> groupedProductTypes;
	
	
	public ProductTypeLister(PersistenceManager persistenceManager, SecurityFilter filter) {
		super();
		this.persistenceManager = persistenceManager;
		this.filter = filter;
	}

	public List<String> getGroups() {
		if (orderedProductTypeGroups == null) {
			QueryBuilder<String> query = new QueryBuilder<String>(ProductTypeGroup.class, filter).setSimpleSelect("name").addOrder("orderIdx");
			orderedProductTypeGroups = persistenceManager.findAll(query);
		}
		return orderedProductTypeGroups;
	}

	public List<ListingPair> getGroupedProductTypes(String group) {
		if (groupedProductTypes == null) {
			groupedProductTypes = new HashMap<String, List<ListingPair>>();
			
			
			for (String groupName : getGroups()) {
				List<ListingPair> types = findProductTypesForGroup(groupName);
				assignGroupToList(groupName, types);
			}
		}
		
		List<ListingPair> list = groupedProductTypes.get(group);
		return list;
	}

	public List<ListingPair> getProductTypes() {
		if (productTypes == null) {
			productTypes = persistenceManager.findAllLP(ProductType.class, filter, "name");
		}
		return productTypes;
	}
	
	
	private void assignGroupToList(String groupName, List<ListingPair> types) {
		if (!types.isEmpty()) {
			Collections.sort(types);
			groupedProductTypes.put(groupName, types);
		}
	}

	private List<ListingPair> findProductTypesForGroup(String groupName) {
		List<ListingPair> types = new ArrayList<ListingPair>();
		
		for (ListingPair type : persistenceManager.findAllLP(ProductType.class, filter, "group.name")) {
			if (groupName.equals(type.getName())) {
				types.add(getProductTypeListingPairForId(type.getId()));
			}
		}
		
		return types;
	}

	private ListingPair getProductTypeListingPairForId(Long productTypeId) {
		ListingPair tmpListingToFind = new ListingPair(productTypeId, "no_name");
		
		int indexOfRealListingPair = getProductTypes().indexOf(tmpListingToFind);
		return getProductTypes().get(indexOfRealListingPair);
	}

	
}
