package com.n4systems.model.autoattribute;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AutoAttributeByAssetTypeLoader extends SecurityFilteredLoader<AutoAttributeCriteria> {
	private AssetType type;
	
	public AutoAttributeByAssetTypeLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AutoAttributeCriteria load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		builder.addWhere(WhereClauseFactory.create("assetType", type));
		builder.addPostFetchPaths("inputs", "outputs");
		
		
		AutoAttributeCriteria criteria = builder.getSingleResult(em);
		return criteria;
	}

	public AutoAttributeByAssetTypeLoader setType(AssetType type) {
		this.type = type;
		return this;
	}

}
