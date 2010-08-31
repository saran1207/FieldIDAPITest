package com.n4systems.services.safetyNetwork;

import java.util.List;
import java.util.Set;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;

public interface CatalogService {

	public Tenant getTenant();
	
	public boolean hasCatalog();
	
	public Set<Long> getProductTypeIdsPublished();

	public List<ListingPair> getPublishedProductTypesLP();
	
	public Set<Long> getInspectionTypeIdsPublished();

	public List<ListingPair> getPublishedInspectionTypesLP();

	public Catalog publishProductTypes(Set<ProductType> productTypes);
	
	public Catalog publishInspectionTypes(Set<InspectionType> inspectionTypes);

	public ProductType getPublishedProductType(Long productTypeId, String...postFetchFields);
	
	public Set<ProductTypeGroup> getProductTypeGroupsFor(Set<Long> productTypeIds);
	
	public List<Long> getAllPublishedSubTypesFor(Long productTypeIds);
	
	public List<Long> getAllPublishedSubTypesFor(Set<Long> productTypeIds);
	
	public Long getAutoAttributeCountFor(Long productTypeId);

	public Set<ListingPair> getInspectionTypeGroupsFor(Set<Long> inspectionTypeIds);
	
	public InspectionType getPublishedInspectionType(Long inspectionTypeId);
	
	public List<StateSet> getStateSetsUsedIn(Set<Long> inspectionTypeIds);
	
	public AutoAttributeCriteria getCriteriaFor(Long productTypeId);
	
	public Pager<AutoAttributeDefinition> getDefinitionPageFor(Long productTypeId, int pageNumber, int pageSize);
	
	public Set<Long> getPublishedInspectionTypeIdsConnectedTo(Set<Long> productTypeIds);
}