package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SavedReportService extends SavedSearchService<SavedReportItem, EventReportCriteriaModel> {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;
    private @Autowired EventTypeService eventTypeService;

    @Override
    protected void storeTransientColumns(EventReportCriteriaModel searchCriteria) {
        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

        AssetTypeGroup assetTypeGroup = searchCriteria.getAssetTypeGroup();
        EventTypeGroup eventTypeGroup = searchCriteria.getEventTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumnsForReporting(searchCriteria.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));
        List<ColumnMappingGroupView> dynamicEventColumns = dynamicColumnsService.getDynamicEventColumnsForReporting(searchCriteria.getEventType(), eventTypeService.getEventTypes(assetTypeGroup == null ? null : eventTypeGroup.getId()));

        searchCriteria.setDynamicAssetColumnGroups(dynamicAssetColumns);
        searchCriteria.setDynamicEventColumnGroups(dynamicEventColumns);
        searchCriteria.setColumnGroups(reportConfiguration.getColumnGroups());
    }

}

