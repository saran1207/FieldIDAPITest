package com.n4systems.model.product;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetByMobileGuidLoader extends SecurityFilteredLoader<Asset> {

	private String mobileGuid;
	
	public AssetByMobileGuidLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Asset load(EntityManager em, SecurityFilter filter) {
		
		QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		
		return query.getSingleResult(em);
	}

	public AssetByMobileGuidLoader setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
	}







}
