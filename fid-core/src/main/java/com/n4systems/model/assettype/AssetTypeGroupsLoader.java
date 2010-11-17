package com.n4systems.model.assettype;

import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class AssetTypeGroupsLoader extends ListLoader<AssetTypeGroup> {

    public AssetTypeGroupsLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<AssetTypeGroup> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<AssetTypeGroup> queryBuilder = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, filter);

        queryBuilder.addOrder("name");

        return queryBuilder.getResultList(em);
    }

}
