package com.n4systems.services.safetyNetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.StateSet;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.services.safetyNetwork.exception.NotPublishedException;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.SecurityFilter;
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
		super();
		this.persistenceManager = persistenceManager;
		this.tenant = tenant;
		filter = new SecurityFilter(tenant.getId()).setDefaultTargets();
	}

	public Set<Long> getProductTypeIdsPublished() {
		Set<Long> productTypeIds = new HashSet<Long>();

		Catalog catalog = getCatalog();
		if (catalog != null) {
			for (ProductType publishedType : catalog.getPublishedProductTypes()) {
				productTypeIds.add(publishedType.getId());
			}
		}
		return productTypeIds;
	}

	public List<ListingPair> getPublishedProductTypesLP() {
		Set<Long> productTypeIdsPublished = getProductTypeIdsPublished();
		if (productTypeIdsPublished.isEmpty()) {
			return new ArrayList<ListingPair>();
		}
		
		QueryBuilder<ListingPair> productTypesQuery = new QueryBuilder<ListingPair>(ProductType.class, filter);
		
		productTypesQuery.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", "name")).addWhere(Comparator.IN, "ids", "id", productTypeIdsPublished);
		productTypesQuery.addOrder("name");

		return persistenceManager.findAll(productTypesQuery);
	}

	public Catalog publishProductTypes(Set<ProductType> productTypes) {
		Catalog catalog = getCatalog();
		productTypes.addAll(getAdditionalSubTypes(productTypes));
		catalog.setPublishedProductTypes(productTypes);
		return persistenceManager.update(catalog);
	}

	private Set<ProductType> getAdditionalSubTypes(Set<ProductType> productTypes) {
		Set<ProductType> additionalSubTypesRequiredForPublishing = new HashSet<ProductType>();
		for (ProductType productType : productTypes) {
			persistenceManager.reattchAndFetch(productType, "subTypes");
			if (!productType.getSubTypes().isEmpty()) {
				for (ProductType subType : productType.getSubTypes()) {
					if (!productTypes.contains(subType)) {
						additionalSubTypesRequiredForPublishing.add(subType);
					}
				}
			}
		}
		return additionalSubTypesRequiredForPublishing;
	}

	public Set<Long> getInspectionTypeIdsPublished() {
		Set<Long> inspectionTypeIds = new HashSet<Long>();
		Catalog catalog = getCatalog();
		if (catalog != null) {
			for (InspectionType publishedType : catalog.getPublishedInspectionTypes()) {
				inspectionTypeIds.add(publishedType.getId());
			}
		}
		return inspectionTypeIds;
	}

	public List<ListingPair> getPublishedInspectionTypesLP() {
		Set<Long> inspectionTypeIdsPublished = getInspectionTypeIdsPublished();
		
		if (inspectionTypeIdsPublished.isEmpty()) {
			return new ArrayList<ListingPair>();
		}
		
		QueryBuilder<ListingPair> inspectionTypesQuery = new QueryBuilder<ListingPair>(InspectionType.class, new SecurityFilter(tenant.getId()).setDefaultTargets());
		inspectionTypesQuery.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", "name")).addWhere(Comparator.IN, "ids", "id", inspectionTypeIdsPublished);
		inspectionTypesQuery.addOrder("name");

		return persistenceManager.findAll(inspectionTypesQuery);
	}

	public Catalog publishInspectionTypes(Set<InspectionType> inspectionTypes) {
		Catalog catalog = getCatalog();
		catalog.setPublishedInspectionTypes(inspectionTypes);
		return persistenceManager.update(catalog);
	}

	private Catalog getCatalog() {
		QueryBuilder<Catalog> query = new QueryBuilder<Catalog>(Catalog.class).addSimpleWhere("tenant", tenant).addPostFetchPaths("publishedProductTypes", "publishedInspectionTypes");
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

	public Set<ProductTypeGroup> getProductTypeGroupsFor(Set<Long> productTypeIds) {
		QueryBuilder<ProductTypeGroup> importingProductTypeGroups = new QueryBuilder<ProductTypeGroup>(ProductType.class, filter);
		applyPublishedProductTypeFilter(importingProductTypeGroups);
		importingProductTypeGroups.setSelectArgument(new SimpleSelect("group"));
		importingProductTypeGroups.addWhere(Comparator.IN, "ids", "id", productTypeIds);
		importingProductTypeGroups.addOrder("group.name");

		return new HashSet<ProductTypeGroup>(persistenceManager.findAll(importingProductTypeGroups));
	}

	public ProductType getPublishedProductType(Long productTypeId, String...postfetchField) {
		isProductTypePublished(productTypeId);
		return persistenceManager.find(ProductType.class, productTypeId, getTenant(), postfetchField);
	}

	public List<Long> getAllPublishedSubTypesFor(Long productTypeId) {
		Set<Long> productTypeIds = new HashSet<Long>();
		productTypeIds.add(productTypeId);
		return getAllPublishedSubTypesFor(productTypeIds);
	}

	public List<Long> getAllPublishedSubTypesFor(Set<Long> productTypeIds) {
		QueryBuilder<Long> subProductIds = new QueryBuilder<Long>(ProductType.class, filter);
		SimpleSelect selectSubProductId = new SimpleSelect("subType.id", true);
		applyPublishedProductTypeFilter(subProductIds);
		selectSubProductId.setDistinct(true);
		subProductIds.setSelectArgument(selectSubProductId);
		subProductIds.addLeftJoin("subTypes", "subType").addWhere(Comparator.IN, "ids", "id", productTypeIds);
		subProductIds.addWhere(new WhereParameter<Set<Long>>(Comparator.IN, "subTypePublishedIds", "subType.id", getProductTypeIdsPublished(), null, true));
		return persistenceManager.findAll(subProductIds);
	}

	private void applyPublishedProductTypeFilter(QueryBuilder<?> query) {
		query.addWhere(Comparator.IN, "publishedIds", "id", getProductTypeIdsPublished());
	}

	public Long getAutoAttributeCountFor(Long productTypeId) {
		isProductTypePublished(productTypeId);
		
		QueryBuilder<AutoAttributeDefinition> definisionCountQuery = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, filter);
		definisionCountQuery.addWhere(Comparator.EQ, "productTypeId", "criteria.productType.id", productTypeId);
		return persistenceManager.findCount(definisionCountQuery);
	}

	public Set<ListingPair> getInspectionTypeGroupsFor(Set<Long> inspectionTypeIds) {
		QueryBuilder<ListingPair> importingInspectionTypeGroups = new QueryBuilder<ListingPair>(InspectionType.class, filter);
		importingInspectionTypeGroups.setSelectArgument(new NewObjectSelect(ListingPair.class, "group.id", "group.name"));
		importingInspectionTypeGroups.addWhere(Comparator.IN, "ids", "id", inspectionTypeIds);
		importingInspectionTypeGroups.addWhere(Comparator.IN, "publishedIds", "id", getInspectionTypeIdsPublished());
		importingInspectionTypeGroups.addOrder("group.name");

		return new HashSet<ListingPair>(persistenceManager.findAll(importingInspectionTypeGroups));
	}

	public InspectionType getPublishedInspectionType(Long inspectionTypeId) {
		if (!getInspectionTypeIdsPublished().contains(inspectionTypeId)) {
			throw new NotPublishedException("not published");
		}
		return persistenceManager.find(InspectionType.class, inspectionTypeId, getTenant().getId(), "supportedProofTests", "sections", "infoFieldNames");
	}

	public List<StateSet> getStateSetsUsedIn(Set<Long> inspectionTypeIds) {
		List<StateSet> originalStateSets = new ArrayList<StateSet>();
		if (!inspectionTypeIds.isEmpty()) {
			QueryBuilder<Long> usedSectionsInInspectionTypesQuery = new QueryBuilder<Long>(InspectionType.class, filter); 
			usedSectionsInInspectionTypesQuery.addRequiredLeftJoin("sections", "section").setSelectArgument(new SimpleSelect("section.id", true));
			usedSectionsInInspectionTypesQuery.addWhere(Comparator.IN, "ids", "id", inspectionTypeIds);
			usedSectionsInInspectionTypesQuery.addWhere(Comparator.IN, "publishedIds", "id", getInspectionTypeIdsPublished());
			
			QueryBuilder<StateSet> usedStateSetsInInspectionTypesQuery = new QueryBuilder<StateSet>(CriteriaSection.class, filter);
			SimpleSelect selectStates = new SimpleSelect("oneCriteria.states", true);
			selectStates.setDistinct(true);
			usedStateSetsInInspectionTypesQuery.addRequiredLeftJoin("criteria", "oneCriteria").setSelectArgument(selectStates);
			usedStateSetsInInspectionTypesQuery.addWhere(Comparator.IN, "ids", "id", persistenceManager.findAll(usedSectionsInInspectionTypesQuery));
			
			originalStateSets = persistenceManager.findAll(usedStateSetsInInspectionTypesQuery);
		}
		return originalStateSets;
	}

	public AutoAttributeCriteria getCriteriaFor(Long productTypeId) {
		isProductTypePublished(productTypeId);
		
		QueryBuilder<AutoAttributeCriteria> query = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		query.addSimpleWhere("productType.id", productTypeId);
		query.addPostFetchPaths("inputs", "outputs");
		return persistenceManager.find(query);
	}

	
	private void isProductTypePublished(Long productTypeId) {
		if (!getProductTypeIdsPublished().contains(productTypeId)) {
			throw new NotPublishedException("not published.");
		}
	}

	public Pager<AutoAttributeDefinition> getDefinitionPageFor(Long productTypeId, int pageNumber, int pageSize) {
		isProductTypePublished(productTypeId);
		
		QueryBuilder<AutoAttributeDefinition> query = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, filter);
		query.addSimpleWhere("criteria.productType.id", productTypeId).addOrder("id").addPostFetchPaths("outputs");
		return persistenceManager.findAllPaged(query, pageNumber, pageSize);
	}

	public Set<Long> getPublishedInspectionTypeIdsConnectedTo(Set<Long> productTypeIds) {
		Set<Long> inspectionTypeIdsPublished = getInspectionTypeIdsPublished();
		if (inspectionTypeIdsPublished.isEmpty() || productTypeIds.isEmpty()) {
			return new HashSet<Long>();
		}
		
		SimpleSelect inspectionTypeId = new SimpleSelect("inspectionType.id", true);
		inspectionTypeId.setDistinct(true);
		
		QueryBuilder<Long> additionalInspectionTypeIdQuery = new QueryBuilder<Long>(AssociatedInspectionType.class, filter);
		additionalInspectionTypeIdQuery.setSelectArgument(inspectionTypeId);
		additionalInspectionTypeIdQuery.addWhere(Comparator.IN, "ptIds", "productType.id", productTypeIds);
		
		additionalInspectionTypeIdQuery.addWhere(new WhereParameter<Collection<Long>>(Comparator.IN, "inspectionTypeIds", "inspectionType.id", inspectionTypeIdsPublished, null, true));
		
		
		return new HashSet<Long>(persistenceManager.findAll(additionalInspectionTypeIdQuery));
	}
	
	public boolean hasCatalog() {
		Catalog catalog = getCatalog();
		return (catalog != null && catalog.hasTypesPublished());
	}
}
