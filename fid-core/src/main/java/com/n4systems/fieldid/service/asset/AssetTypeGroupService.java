package com.n4systems.fieldid.service.asset;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.search.SavedSearchRemoveFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetTypeGroupService extends CrudService<AssetTypeGroup> {
	private static final Logger logger= Logger.getLogger(AssetTypeGroupService.class);

    @Autowired
    private SavedReportService savedReportService;

	public AssetTypeGroupService() {
		super(AssetTypeGroup.class);
	}

    @Transactional
	public AssetTypeGroupRemovalSummary testDelete(AssetTypeGroup group) {
		AssetTypeGroupRemovalSummary summary = new AssetTypeGroupRemovalSummary(group);

        try {
            QueryBuilder<AssetType> countQuery = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
            countQuery.addSimpleWhere("group", group);
            summary.setAssetTypesConnected(persistenceService.count(countQuery));

            final QueryBuilder<SearchCriteria> savedItemCountQuery = createTenantSecurityBuilder(SearchCriteria.class);
            savedItemCountQuery.addSimpleWhere("assetTypeGroup", group);
            final List<SearchCriteria> all = persistenceService.findAll(savedItemCountQuery);

            summary.setSavedReportsConnected((long)all.size());
        }
		catch ( InvalidQueryException e) {
            logger.error("bad removal summary query", e);
            summary = null;
        }
		return summary;
	}

    @Transactional
	public void deleteAssetTypeGroup(final AssetTypeGroup group) {
        savedReportService.deleteAllSavedSearchesMatching(new SavedSearchRemoveFilter() {
            @Override
            public boolean removeThisSearch(SearchCriteria searchCriteria) {
                return searchCriteria.getAssetTypeGroup() != null && group.getId().equals(searchCriteria.getAssetTypeGroup().getId());
            }
        });

		AssetTypeGroup groupToDelete = persistenceService.find(AssetTypeGroup.class, group.getId());

        Map<String,Object> params = new HashMap<String,Object>();
		params.put("group", groupToDelete);
        Query clearAssetTypesQuery = persistenceService.createQuery("UPDATE " + AssetType.class.getName() + " assetType SET assetType.group = null WHERE assetType.group = :group", params);
        clearAssetTypesQuery.executeUpdate();

        try {
            persistenceService.delete(groupToDelete);
        }
        catch (Exception e) {
            logger.error("could not delete asset type group: " + group.getDisplayName(), e);
        }
	}

    public AssetTypeGroupRemovalSummary getRemovalSummary(AssetTypeGroup group) {
        return testDelete(group);
    }

    public AssetTypeGroup getAssetTypeGroupById(Long id) {
        return persistenceService.find(AssetTypeGroup.class, id);
    }

    public List<AssetTypeGroup> getAllAssetTypeGroups() {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        query.addOrder("orderIdx");
        return persistenceService.findAll(query);
    }

    public boolean isNameExists(String name) {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        query.addWhere(WhereClauseFactory.create("name", name));
        return persistenceService.exists(query);
    }

    public AssetTypeGroup saveAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        try {
            assetTypeGroup = (AssetTypeGroup) persistenceService.saveOrUpdate(assetTypeGroup);
        }
        catch (Exception e) {
            logger.error("could not save asset type group: " + assetTypeGroup.getDisplayName(), e);
        }

        return assetTypeGroup;
    }

    public Long getNextAvailableIndex() {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        return persistenceService.count(query);
    }

    public void updateAllAssetTypeGroups(List<AssetTypeGroup> groupList) {

        for (int i = 0; i < groupList.size(); i++) {
            AssetTypeGroup group = groupList.get(i);
            group.setOrderIdx(new Long(i));
        }

        try {
            persistenceService.updateAll(groupList, getCurrentUser().getId());
        }
        catch (Exception e) {
            logger.error("could not update asset type groups" + e);
        }
    }


    public Long countAssetTypeGroups() {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);

        return persistenceService.count(query);
    }
}
