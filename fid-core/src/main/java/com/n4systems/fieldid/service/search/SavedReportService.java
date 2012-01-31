package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class SavedReportService extends SavedSearchService<SavedReportItem, EventReportCriteriaModel> {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;
    private @Autowired EventTypeService eventTypeService;

    @Override
    public void removeLastSavedSearch(User user) {
        if (user.getLastRunReport() != null) {
            final EventReportCriteriaModel lastRunReport = user.getLastRunReport();
            user.setLastRunReport(null);
            persistenceService.update(user);
            persistenceService.delete(lastRunReport);
        }
    }

    @Override
    public EventReportCriteriaModel retrieveLastSearch() {
        final EventReportCriteriaModel lastRunReport = getCurrentUser().getLastRunReport();
        storeTransientColumns(lastRunReport);
        enableSelectedColumns(lastRunReport, lastRunReport.getColumns());
        return lastRunReport;
    }

    @Override
    protected void storeLastSearchInUser(User user, EventReportCriteriaModel searchCriteria) {
        user.setLastRunReport(searchCriteria);
    }

    @Override
    protected void storeTransientColumns(EventReportCriteriaModel searchCriteria) {
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

        AssetTypeGroup assetTypeGroup = searchCriteria.getAssetTypeGroup();
        EventTypeGroup eventTypeGroup = searchCriteria.getEventTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumnsForReporting(searchCriteria.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));
        List<ColumnMappingGroupView> dynamicEventColumns = dynamicColumnsService.getDynamicEventColumnsForReporting(searchCriteria.getEventType(), eventTypeService.getEventTypes(eventTypeGroup == null ? null : eventTypeGroup.getId()));

        searchCriteria.setDynamicAssetColumnGroups(dynamicAssetColumns);
        searchCriteria.setDynamicEventColumnGroups(dynamicEventColumns);
        searchCriteria.setColumnGroups(reportConfiguration.getColumnGroups());
    }

    @Transactional
    public void deleteAllSavedReportsWith(AssetTypeGroup assetTypeGroup) {
        final QueryBuilder<User> query = createTenantSecurityBuilder(User.class);
        final List<User> users = persistenceService.findAll(query);

        for (User user : users) {
            List<SavedItem> adjustedSavedItems = new ArrayList<SavedItem>(user.getSavedItems());
            for (SavedItem item : user.getSavedItems()) {
                if (assetTypeGroup.getId().equals(getId(item.getSearchCriteria().getAssetTypeGroup()))) {
                    adjustedSavedItems.remove(item);
                }
            }
            user.setSavedItems(adjustedSavedItems);

            if (user.getLastRunReport() != null && assetTypeGroup.getId().equals(getId(user.getLastRunReport().getAssetTypeGroup()))) {
                user.setLastRunReport(null);
            }

            if (user.getLastRunSearch() != null && assetTypeGroup.getId().equals(getId(user.getLastRunSearch().getAssetTypeGroup()))) {
                user.setLastRunSearch(null);
            }

            persistenceService.update(user);
        }
    }

}
