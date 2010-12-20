package com.n4systems.model.assettype;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AutoAttributeCriteriaByAssetTypeIdLoader extends SecurityFilteredLoader<AutoAttributeCriteria> implements IdLoader<AutoAttributeCriteriaByAssetTypeIdLoader> {
	private Long assetTypeId;

	public AutoAttributeCriteriaByAssetTypeIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AutoAttributeCriteria load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter);
		builder.addSimpleWhere("assetType.id", assetTypeId);
		builder.addFetch("inputs");
		
		AutoAttributeCriteria aaCriteria = builder.getSingleResult(em);
		return aaCriteria;
	}

	public void setAssetTypeId(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	@Override
	public AutoAttributeCriteriaByAssetTypeIdLoader setId(Long id) {
		setAssetTypeId(id);
		return this;
	}
}
