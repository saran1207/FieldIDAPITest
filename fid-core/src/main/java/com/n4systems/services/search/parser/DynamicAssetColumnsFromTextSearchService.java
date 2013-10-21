package com.n4systems.services.search.parser;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DynamicAssetColumnsFromTextSearchService extends FieldIdPersistenceService {

    private @Autowired SearchParserService searchParserService;
    private @Autowired PersistenceService persistenceService;

    private @Autowired DynamicColumnsService dynamicColumnsService;

    public void updateColumns(AssetSearchCriteria criteria) {
        if (criteria.getQuery() == null) {
            return;
        }
        SearchQuery searchQuery = searchParserService.createSearchQuery(criteria.getQuery());
        QueryTerm assetTypeTerm = searchQuery.getTermForAttribute(AssetIndexField.TYPE.getField());
        QueryTerm assetTypeGroupTerm = searchQuery.getTermForAttribute(AssetIndexField.TYPE_GROUP.getField());

        AssetTypeGroup assetTypeGroup = null;
        AssetType assetType = null;

        if (assetTypeGroupTerm != null) {
            String assetTypeGroupName = assetTypeGroupTerm.getValue().getString();
            assetTypeGroup = persistenceService.findByName(AssetTypeGroup.class, assetTypeGroupName);
        }

        if (assetTypeTerm != null) {
            String assetTypeName = assetTypeTerm.getValue().getString();
            assetType = persistenceService.findByName(AssetType.class, assetTypeName);
        }

        if ((assetType != null && !assetType.equals(criteria.getAssetType()) || (assetTypeGroup!=null && !assetTypeGroup.equals(criteria.getAssetTypeGroup())))) {
            // We only update the columns if we found a change in the searched for asset type or asset type group.
            QueryBuilder<AssetType> availableAssetTypesBuilder = createUserSecurityBuilder(AssetType.class);
            if (assetTypeGroup != null) {
                availableAssetTypesBuilder.addSimpleWhere("group", assetTypeGroup);
            }

            List<AssetType> availableAssetTypes = persistenceService.findAll(availableAssetTypesBuilder);
            List<ColumnMappingGroupView> dynamicAssetColumnsForSearch = dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);

            criteria.setDynamicAssetColumnGroups(dynamicAssetColumnsForSearch);

            criteria.setAssetType(assetType);
            criteria.setAssetTypeGroup(assetTypeGroup);
        }

    }
}
