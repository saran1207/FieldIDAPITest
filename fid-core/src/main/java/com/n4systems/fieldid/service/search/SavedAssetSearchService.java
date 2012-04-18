package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SavedAssetSearchService extends SavedSearchService<SavedSearchItem, AssetSearchCriteria> {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;

    @Override
    public void removeLastSavedSearch(User user) {
        if (user.getLastRunSearch() != null) {
            final AssetSearchCriteria lastRunSearch = user.getLastRunSearch();
            user.setLastRunSearch(null);
            persistenceService.update(user);
            persistenceService.delete(lastRunSearch);
        }
    }

    @Override
    public AssetSearchCriteria retrieveLastSearch() {
        final AssetSearchCriteria lastRunSearch = getCurrentUser().getLastRunSearch();
        lastRunSearch.setReportAlreadyRun(true);
        storeTransientColumns(lastRunSearch);
        enableSelectedColumns(lastRunSearch, lastRunSearch.getColumns());
        return lastRunSearch;
    }

    @Override
    protected void storeLastSearchInUser(User user, AssetSearchCriteria searchCriteria) {
        user.setLastRunSearch(searchCriteria);
    }

    @Override
    protected void storeTransientColumns(AssetSearchCriteria searchCriteria) {
        ReportConfiguration reportConfiguration = new AssetColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

        AssetTypeGroup assetTypeGroup = searchCriteria.getAssetTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumnsForReporting(searchCriteria.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));

        searchCriteria.setDynamicAssetColumnGroups(dynamicAssetColumns);
        searchCriteria.setColumnGroups(reportConfiguration.getColumnGroups());
    }

    @Override
    protected SavedSearchItem createSavedItem(SavedSearchItem savedItem) {
        return savedItem.copy(new SavedSearchItem());
    }

}
