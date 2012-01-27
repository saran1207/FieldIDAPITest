package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SavedAssetSearchService extends SavedSearchService<SavedSearchItem, AssetSearchCriteriaModel> {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;

    @Override
    public AssetSearchCriteriaModel retrieveLastSearch() {
        final AssetSearchCriteriaModel lastRunSearch = getCurrentUser().getLastRunSearch();
        storeTransientColumns(lastRunSearch);
        enableSelectedColumns(lastRunSearch, lastRunSearch.getColumns());
        return lastRunSearch;
    }

    @Override
    protected void storeLastSearchInUser(User user, AssetSearchCriteriaModel searchCriteria) {
        user.setLastRunSearch(searchCriteria);
    }

    @Override
    protected void storeTransientColumns(AssetSearchCriteriaModel searchCriteria) {
        ReportConfiguration reportConfiguration = new AssetColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

        AssetTypeGroup assetTypeGroup = searchCriteria.getAssetTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumnsForReporting(searchCriteria.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));

        searchCriteria.setDynamicAssetColumnGroups(dynamicAssetColumns);
        searchCriteria.setColumnGroups(reportConfiguration.getColumnGroups());
    }

}
