package com.n4systems.services.safetyNetwork;

import java.util.List;
import java.util.Set;

import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.EventType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;

public interface CatalogService {

	public Tenant getTenant();
	
	public boolean hasCatalog();
	
	public Set<Long> getAssetTypeIdsPublished();

	public List<ListingPair> getPublishedAssetTypesLP();
	
	public Set<AssetTypeGroup> getPublishedAssetTypeGroups(); 
	
	public Set<Long> getEventTypeIdsPublished();

	public List<ListingPair> getPublishedEventTypesLP();

	public Catalog publishAssetTypes(Set<AssetType> assetTypes);
	
	public Catalog publishEventTypes(Set<EventType> eventTypes);

	public AssetType getPublishedAssetType(Long assetTypeId, String...postFetchFields);
	
	public Set<AssetTypeGroup> getAssetTypeGroupsFor(Set<Long> assetTypeIds);
	
	public List<Long> getAllPublishedSubTypesFor(Long assetTypeIds);
	
	public List<Long> getAllPublishedSubTypesFor(Set<Long> assetTypeIds);
	
	public Long getAutoAttributeCountFor(Long assetTypeId);

	public Set<ListingPair> getEventTypeGroupsFor(Set<Long> eventTypeIds);
	
	public EventType getPublishedEventType(Long eventTypeId);
	
	public List<StateSet> getStateSetsUsedIn(Set<Long> eventTypeIds);
	
	public AutoAttributeCriteria getCriteriaFor(Long assetTypeId);
	
	public Pager<AutoAttributeDefinition> getDefinitionPageFor(Long assetTypeId, int pageNumber, int pageSize);
	
	public Set<Long> getPublishedEventTypeIdsConnectedTo(Set<Long> assetTypeIds);
}
