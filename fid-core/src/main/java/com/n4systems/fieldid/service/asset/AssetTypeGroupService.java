package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.search.SavedSearchRemoveFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class AssetTypeGroupService extends FieldIdPersistenceService {

    @Autowired
    private SavedReportService savedReportService;

    @Transactional
	public AssetTypeGroupRemovalSummary testDelete(AssetTypeGroup group) {
		AssetTypeGroupRemovalSummary summary = new AssetTypeGroupRemovalSummary(group);
		QueryBuilder<AssetType> countQuery = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
		countQuery.addSimpleWhere("group", group);
        summary.setAssetTypesConnected(persistenceService.count(countQuery));

        final QueryBuilder<SearchCriteria> savedItemCountQuery = createTenantSecurityBuilder(SearchCriteria.class);
        savedItemCountQuery.addSimpleWhere("assetTypeGroup", group);
        final List<SearchCriteria> all = persistenceService.findAll(savedItemCountQuery);

        summary.setSavedReportsConnected((long)all.size());

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

        persistenceService.delete(groupToDelete);
	}

}
