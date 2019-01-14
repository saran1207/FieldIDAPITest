package com.n4systems.fieldid.service.catalog;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PublishedCatalogService extends FieldIdPersistenceService {

    @Autowired
    ConfigService configService;


    public Tenant getHouseAccountTenant() {
        String houseAccountName = configService.getString(ConfigEntry.HOUSE_ACCOUNT_NAME);
        for (TypedOrgConnection connection : getConnections()) {
            if (connection.getConnectedOrg().getTenant().getName().equals(houseAccountName)) {
                Tenant houseAccountTenant = connection.getConnectedOrg().getTenant();
                return houseAccountTenant;
            }
        }
        throw new RuntimeException("Unable to find tenant for House Account '" + houseAccountName + "'");
    }

    public Set<AssetTypeGroup> getPublishedAssetTypeGroups(Tenant tenant) {
        Set<Long> assetTypeIds = new HashSet<Long>();
        for (ListingPair assetType : getPublishedAssetTypesLP(tenant)){
            assetTypeIds.add(assetType.getId());
        }
        return getAssetTypeGroupsFor(assetTypeIds, tenant);

    }

    public List<ListingPair> getPublishedAssetTypesForGroup(Tenant tenant, String groupName) {

        List<ListingPair> groupedTypes = new ArrayList<ListingPair>();

        for (ListingPair assetType : getPublishedAssetTypesLP(tenant)) {
            QueryBuilder<AssetType> query = new QueryBuilder<AssetType>(AssetType.class, new TenantOnlySecurityFilter(tenant.getId()));
            query.addWhere(WhereParameter.Comparator.EQ, "id", "id", assetType.getId());
            AssetType retrievedAssetType = persistenceService.find(query);
            postFetchFields(retrievedAssetType, "infoFields");
            if (retrievedAssetType.getGroup() != null) {
                if (retrievedAssetType.getGroup().getName().equals(groupName)){
                    groupedTypes.add(assetType);
                }
            }
        }
        return groupedTypes;
    }

    public List<ListingPair> getPublishedAssetTypesUngrouped(Tenant tenant) {

        List<ListingPair> unGroupedTypes = new ArrayList<ListingPair>();

        for (ListingPair assetType : getPublishedAssetTypesLP(tenant)) {
            QueryBuilder<AssetType> query = new QueryBuilder<AssetType>(AssetType.class, new TenantOnlySecurityFilter(tenant.getId()));
            query.addWhere(WhereParameter.Comparator.EQ, "id", "id", assetType.getId());
            AssetType retrievedAssetType = persistenceService.find(query);
            postFetchFields(retrievedAssetType, "infoFields");
            if (retrievedAssetType.getGroup() == null) {
                unGroupedTypes.add(assetType);
            }
        }
        return unGroupedTypes;
    }

    private List<TypedOrgConnection> getConnections() {
        QueryBuilder<TypedOrgConnection> query = createUserSecurityBuilder(TypedOrgConnection.class);
        query.addOrder("connectedOrg.name");
        return persistenceService.findAll(query);
    }

    public List<ListingPair> getPublishedAssetTypesLP(Tenant tenant) {
        Set<Long> assetTypeIdsPublished = getAssetTypeIdsPublished(tenant);
        if (assetTypeIdsPublished.isEmpty()) {
            return new ArrayList<ListingPair>();
        }
        QueryBuilder<ListingPair> assetTypesQuery = new QueryBuilder<ListingPair>(AssetType.class, new TenantOnlySecurityFilter(tenant.getId()));
        assetTypesQuery.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", "name")).addWhere(WhereParameter.Comparator.IN, "ids", "id", assetTypeIdsPublished);
        assetTypesQuery.addOrder("name");
        return persistenceService.findAll(assetTypesQuery);
    }

    private <T> T postFetchFields(T entity, String... postFetchFields) {
        return PostFetcher.postFetchFields(entity, postFetchFields);
    }

    public Set<AssetTypeGroup> getAssetTypeGroupsFor(Set<Long> assetTypeIds, Tenant tenant) {
        QueryBuilder<AssetTypeGroup> importingAssetTypeGroups = new QueryBuilder<AssetTypeGroup>(AssetType.class, new TenantOnlySecurityFilter(tenant.getId()));
        applyPublishedAssetTypeFilter(importingAssetTypeGroups, tenant);
        importingAssetTypeGroups.setSelectArgument(new SimpleSelect("group"));
        importingAssetTypeGroups.addWhere(WhereParameter.Comparator.IN, "ids", "id", assetTypeIds);
        importingAssetTypeGroups.addOrder("group.name");

        return new HashSet<AssetTypeGroup>(persistenceService.findAll(importingAssetTypeGroups));
    }

    public Set<Long> getEventTypeIdsPublished(Tenant tenant) {
        Set<Long> eventTypeIds = new HashSet<Long>();
        Catalog catalog = getCatalog(tenant);
        if (catalog != null) {
            for (EventType publishedType : catalog.getPublishedEventTypes()) {
                eventTypeIds.add(publishedType.getId());
            }
        }
        return eventTypeIds;
    }

    private Catalog getCatalog(Tenant tenant) {
        QueryBuilder<Catalog> query = new QueryBuilder<Catalog>(Catalog.class, new OpenSecurityFilter()).addSimpleWhere("tenant", tenant).addPostFetchPaths("publishedAssetTypes", "publishedEventTypes");
        Catalog catalog = persistenceService.find(query);
        if (catalog == null) {
            catalog = new Catalog();
            catalog.setTenant(tenant);
        }
        return catalog;
    }

    public Set<Long> getAssetTypeIdsPublished(Tenant tenant) {
        Set<Long> assetTypeIds = new HashSet<Long>();

        Catalog catalog = getCatalog(tenant);
        if (catalog != null) {
            for (AssetType publishedType : catalog.getPublishedAssetTypes()) {
                assetTypeIds.add(publishedType.getId());
            }
        }
        return assetTypeIds;
    }

    private void applyPublishedAssetTypeFilter(QueryBuilder<?> query, Tenant tenant) {
        query.addWhere(WhereParameter.Comparator.IN, "publishedIds", "id", getAssetTypeIdsPublished(tenant));
    }

}
