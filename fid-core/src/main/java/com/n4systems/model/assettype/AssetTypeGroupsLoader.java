package com.n4systems.model.assettype;

import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class AssetTypeGroupsLoader extends ListLoader<AssetTypeGroup> {
	
	private String[] postFetchFields = new String[0];
	private String orderBy;

    public AssetTypeGroupsLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<AssetTypeGroup> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<AssetTypeGroup> queryBuilder = new QueryBuilder<AssetTypeGroup>(AssetTypeGroup.class, filter);
        
        if(orderBy == null)
        	queryBuilder.addOrder("name");
        else
        	queryBuilder.addOrder(orderBy);
        queryBuilder.addPostFetchPaths(postFetchFields);

        return queryBuilder.getResultList(em);
    }
    
	public AssetTypeGroupsLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	public AssetTypeGroupsLoader setOrderBy(String orderBy) {
		this.orderBy = orderBy;
		return this;
	}


}
