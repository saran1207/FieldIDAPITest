package com.n4systems.fieldid.service.asset;

import java.util.List;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetTypeService extends FieldIdPersistenceService {

    public List<AssetTypeGroup> getAssetTypeGroupsByOrder() {
        QueryBuilder<AssetTypeGroup> queryBuilder = createUserSecurityBuilder(AssetTypeGroup.class);

        queryBuilder.addOrder("orderIdx");

        return persistenceService.findAll(queryBuilder);
    }

    public List<AssetType> getAssetTypes(Long assetTypeGroupId) {
		QueryBuilder<AssetType> builder = createUserSecurityBuilder(AssetType.class);

		if(assetTypeGroupId != null) {
			if (assetTypeGroupId == -1)
				builder.addWhere(WhereClauseFactory.createIsNull("group.id"));
			else {
				builder.addSimpleWhere("group.id", assetTypeGroupId);
			}
		}

		builder.addOrder("name");
		return persistenceService.findAll(builder);
    }

}
