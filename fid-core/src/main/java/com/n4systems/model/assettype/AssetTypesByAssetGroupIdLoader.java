package com.n4systems.model.assettype;

import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class AssetTypesByAssetGroupIdLoader extends ListLoader<AssetType> {

    private Long assetTypeGroupId;

    public AssetTypesByAssetGroupIdLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<AssetType> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<AssetType> query = new QueryBuilder<AssetType>(AssetType.class, filter);

        if (assetTypeGroupId != null) {
            query.addSimpleWhere("group.id", assetTypeGroupId);
        }

        query.addOrder("name");

        return query.getResultList(em);
    }

    public Long getAssetTypeGroupId() {
        return assetTypeGroupId;
    }

    public void setAssetTypeGroupId(Long assetTypeGroupId) {
        this.assetTypeGroupId = assetTypeGroupId;
    }
}
