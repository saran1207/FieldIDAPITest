package com.n4systems.services.safetyNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventType;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.services.safetyNetwork.exception.NotPublishedException;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CatalogServiceImpl implements CatalogService {

	private final PersistenceManager persistenceManager;
	private final Tenant tenant;
	private final SecurityFilter filter;

	public CatalogServiceImpl(PersistenceManager persistenceManager, Tenant tenant) {
		this.persistenceManager = persistenceManager;
		this.tenant = tenant;
		filter = new TenantOnlySecurityFilter(tenant.getId());
	}

	public Set<Long> getAssetTypeIdsPublished() {
		Set<Long> assetTypeIds = new HashSet<Long>();

		Catalog catalog = getCatalog();
		if (catalog != null) {
			for (AssetType publishedType : catalog.getPublishedAssetTypes()) {
				assetTypeIds.add(publishedType.getId());
			}
		}
		return assetTypeIds;
	}

	public List<ListingPair> getPublishedAssetTypesLP() {
		Set<Long> assetTypeIdsPublished = getAssetTypeIdsPublished();
		if (assetTypeIdsPublished.isEmpty()) {
			return new ArrayList<ListingPair>();
		}
		
		QueryBuilder<ListingPair> assetTypesQuery = new QueryBuilder<ListingPair>(AssetType.class, filter);
		
		assetTypesQuery.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", "name")).addWhere(Comparator.IN, "ids", "id", assetTypeIdsPublished);
		assetTypesQuery.addOrder("name");

		return persistenceManager.findAll(assetTypesQuery);
	}
	
	public Set<AssetTypeGroup> getPublishedAssetTypeGroups() {
		Set<Long> assetTypeIds = new HashSet<Long>();
		for (ListingPair assetType : getPublishedAssetTypesLP()){
			assetTypeIds.add(assetType.getId());
		}
		return getAssetTypeGroupsFor(assetTypeIds);
	}

	public Catalog publishAssetTypes(Set<AssetType> assetTypes) {
		Catalog catalog = getCatalog();
		assetTypes.addAll(getAdditionalSubTypes(assetTypes));
		catalog.setPublishedAssetTypes(assetTypes);
		return persistenceManager.update(catalog);
	}

	private Set<AssetType> getAdditionalSubTypes(Set<AssetType> assetTypes) {
		Set<AssetType> additionalSubTypesRequiredForPublishing = new HashSet<AssetType>();
		for (AssetType assetType : assetTypes) {
			persistenceManager.reattchAndFetch(assetType, "subTypes");
			if (!assetType.getSubTypes().isEmpty()) {
				for (AssetType subType : assetType.getSubTypes()) {
					if (!assetTypes.contains(subType)) {
						additionalSubTypesRequiredForPublishing.add(subType);
					}
				}
			}
		}
		return additionalSubTypesRequiredForPublishing;
	}

	public Set<Long> getEventTypeIdsPublished() {
		Set<Long> eventTypeIds = new HashSet<Long>();
		Catalog catalog = getCatalog();
		if (catalog != null) {
			for (EventType publishedType : catalog.getPublishedEventTypes()) {
				eventTypeIds.add(publishedType.getId());
			}
		}
		return eventTypeIds;
	}

	public List<ListingPair> getPublishedEventTypesLP() {
		Set<Long> eventTypeIdsPublished = getEventTypeIdsPublished();
		
		if (eventTypeIdsPublished.isEmpty()) {
			return new ArrayList<ListingPair>();
		}
		
		QueryBuilder<ListingPair> eventTypesQuery = new QueryBuilder<ListingPair>(EventType.class, new TenantOnlySecurityFilter(tenant.getId()));
		eventTypesQuery.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", "name")).addWhere(Comparator.IN, "ids", "id", eventTypeIdsPublished);
		eventTypesQuery.addOrder("name");

		return persistenceManager.findAll(eventTypesQuery);
	}

	public Catalog publishEventTypes(Set<EventType> eventTypes) {
		Catalog catalog = getCatalog();
		catalog.setPublishedEventTypes(eventTypes);
		return persistenceManager.update(catalog);
	}

	private Catalog getCatalog() {
		QueryBuilder<Catalog> query = new QueryBuilder<Catalog>(Catalog.class, new OpenSecurityFilter()).addSimpleWhere("tenant", tenant).addPostFetchPaths("publishedAssetTypes", "publishedEventTypes");
		Catalog catalog = persistenceManager.find(query);
		if (catalog == null) {
			catalog = new Catalog();
			catalog.setTenant(tenant);
		}
		return catalog;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public Set<AssetTypeGroup> getAssetTypeGroupsFor(Set<Long> assetTypeIds) {
		QueryBuilder<AssetTypeGroup> importingAssetTypeGroups = new QueryBuilder<AssetTypeGroup>(AssetType.class, filter);
		applyPublishedAssetTypeFilter(importingAssetTypeGroups);
		importingAssetTypeGroups.setSelectArgument(new SimpleSelect("group"));
		importingAssetTypeGroups.addWhere(Comparator.IN, "ids", "id", assetTypeIds);
		importingAssetTypeGroups.addOrder("group.name");

		return new HashSet<AssetTypeGroup>(persistenceManager.findAll(importingAssetTypeGroups));
	}

	public AssetType getPublishedAssetType(Long assetTypeId, String...postfetchField) {
		isAssetTypePublished(assetTypeId);
		return persistenceManager.find(AssetType.class, assetTypeId, getTenant(), postfetchField);
	}

	public List<Long> getAllPublishedSubTypesFor(Long assetTypeId) {
		Set<Long> assetTypeIds = new HashSet<Long>();
		assetTypeIds.add(assetTypeId);
		return getAllPublishedSubTypesFor(assetTypeIds);
	}

	public List<Long> getAllPublishedSubTypesFor(Set<Long> assetTypeIds) {
		QueryBuilder<Long> subAssetIds = new QueryBuilder<Long>(AssetType.class, filter);
		SimpleSelect selectSubAssetId = new SimpleSelect("subType.id", true);
		applyPublishedAssetTypeFilter(subAssetIds);
		selectSubAssetId.setDistinct(true);
		subAssetIds.setSelectArgument(selectSubAssetId);
		subAssetIds.addLeftJoin("subTypes", "subType").addWhere(Comparator.IN, "ids", "id", assetTypeIds);
		subAssetIds.addWhere(new WhereParameter<Set<Long>>(Comparator.IN, "subTypePublishedIds", "subType.id", getAssetTypeIdsPublished(), null, true));
		return persistenceManager.findAll(subAssetIds);
	}

	private void applyPublishedAssetTypeFilter(QueryBuilder<?> query) {
		query.addWhere(Comparator.IN, "publishedIds", "id", getAssetTypeIdsPublished());
	}

	public Long getAutoAttributeCountFor(Long assetTypeId) {
		isAssetTypePublished(assetTypeId);
		
		QueryBuilder<AutoAttributeDefinition> definisionCountQuery = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, filter);
		definisionCountQuery.addWhere(Comparator.EQ, "assetTypeId", "criteria.assetType.id", assetTypeId);
		return persistenceManager.findCount(definisionCountQuery);
	}

	public Set<ListingPair> getEventTypeGroupsFor(Set<Long> eventTypeIds) {
		QueryBuilder<ListingPair> importingEventTypeGroups = new QueryBuilder<ListingPair>(EventType.class, filter);
		importingEventTypeGroups.setSelectArgument(new NewObjectSelect(ListingPair.class, "group.id", "group.name"));
		importingEventTypeGroups.addWhere(Comparator.IN, "ids", "id", eventTypeIds);
		importingEventTypeGroups.addWhere(Comparator.IN, "publishedIds", "id", getEventTypeIdsPublished());
		importingEventTypeGroups.addOrder("group.name");

		return new HashSet<ListingPair>(persistenceManager.findAll(importingEventTypeGroups));
	}

	public EventType getPublishedEventType(Long eventTypeId) {
		if (!getEventTypeIdsPublished().contains(eventTypeId)) {
			throw new NotPublishedException("not published");
		}
		return persistenceManager.find(EventType.class, eventTypeId, getTenant().getId(), "supportedProofTests", "sections", "infoFieldNames");
	}

	public List<StateSet> getStateSetsUsedIn(Set<Long> eventTypeIds) {
		List<StateSet> originalStateSets = new ArrayList<StateSet>();
		if (!eventTypeIds.isEmpty()) {
			QueryBuilder<Long> usedSectionsInEventTypesQuery = new QueryBuilder<Long>(EventType.class, filter);
			usedSectionsInEventTypesQuery.addRequiredLeftJoin("sections", "section").setSelectArgument(new SimpleSelect("section.id", true));
			usedSectionsInEventTypesQuery.addWhere(Comparator.IN, "ids", "id", eventTypeIds);
			usedSectionsInEventTypesQuery.addWhere(Comparator.IN, "publishedIds", "id", getEventTypeIdsPublished());
			
			QueryBuilder<StateSet> usedStateSetsInEventTypesQuery = new QueryBuilder<StateSet>(CriteriaSection.class, filter);
			SimpleSelect selectStates = new SimpleSelect("oneCriteria.states", true);
			selectStates.setDistinct(true);
			usedStateSetsInEventTypesQuery.addRequiredLeftJoin("criteria", "oneCriteria").setSelectArgument(selectStates);
			usedStateSetsInEventTypesQuery.addWhere(Comparator.IN, "ids", "id", persistenceManager.findAll(usedSectionsInEventTypesQuery));
			
			originalStateSets = persistenceManager.findAll(usedStateSetsInEventTypesQuery);
		}
		return originalStateSets;
	}

	public AutoAttributeCriteria getCriteriaFor(Long assetTypeId) {
		isAssetTypePublished(assetTypeId);
		
		QueryBuilder<AutoAttributeCriteria> query = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		query.addSimpleWhere("assetType.id", assetTypeId);
		query.addPostFetchPaths("inputs", "outputs");
		return persistenceManager.find(query);
	}

	
	private void isAssetTypePublished(Long assetTypeId) {
		if (!getAssetTypeIdsPublished().contains(assetTypeId)) {
			throw new NotPublishedException("not published.");
		}
	}

	public Pager<AutoAttributeDefinition> getDefinitionPageFor(Long assetTypeId, int pageNumber, int pageSize) {
		isAssetTypePublished(assetTypeId);
		
		QueryBuilder<AutoAttributeDefinition> query = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, filter);
		query.addSimpleWhere("criteria.assetType.id", assetTypeId).addOrder("id").addPostFetchPaths("outputs");
		return persistenceManager.findAllPaged(query, pageNumber, pageSize);
	}

	public Set<Long> getPublishedEventTypeIdsConnectedTo(Set<Long> assetTypeIds) {
		Set<Long> eventTypeIdsPublished = getEventTypeIdsPublished();
		if (eventTypeIdsPublished.isEmpty() || assetTypeIds.isEmpty()) {
			return new HashSet<Long>();
		}
		
		SimpleSelect eventTypeId = new SimpleSelect("eventType.id", true);
		eventTypeId.setDistinct(true);
		
		QueryBuilder<Long> additionalEventTypeIdQuery = new QueryBuilder<Long>(AssociatedEventType.class, filter);
		additionalEventTypeIdQuery.setSelectArgument(eventTypeId);
		additionalEventTypeIdQuery.addWhere(Comparator.IN, "ptIds", "assetType.id", assetTypeIds);
		
		additionalEventTypeIdQuery.addWhere(new WhereParameter<Collection<Long>>(Comparator.IN, "eventTypeIds", "eventType.id", eventTypeIdsPublished, null, true));
		
		
		return new HashSet<Long>(persistenceManager.findAll(additionalEventTypeIdQuery));
	}
	
	public boolean hasCatalog() {
		Catalog catalog = getCatalog();
		return (catalog != null && catalog.hasTypesPublished());
	}
}
