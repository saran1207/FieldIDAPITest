package com.n4systems.services.safetyNetwork;

import com.n4systems.model.*;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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
	
	public ThingEventType getPublishedEventType(Long eventTypeId);
	
	public Collection<ButtonGroup> getStateSetsUsedIn(Set<Long> eventTypeIds);
	
	public AutoAttributeCriteria getCriteriaFor(Long assetTypeId);
	
	public Pager<AutoAttributeDefinition> getDefinitionPageFor(Long assetTypeId, int pageNumber, int pageSize);
	
	public Set<Long> getPublishedEventTypeIdsConnectedTo(Set<Long> assetTypeIds);
}
