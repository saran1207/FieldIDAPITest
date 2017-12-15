package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetByMobileGuidLoader extends SecurityFilteredLoader<Asset> {

	private String mobileGuid;
	private List<String> postFetchFields = new ArrayList<String>();

	public AssetByMobileGuidLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Asset load(EntityManager em, SecurityFilter filter) {
		
		QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, filter);
		query.addSimpleWhere("mobileGUID", mobileGuid);
		if (!postFetchFields.isEmpty()) {
			query.getPostFetchPaths().addAll(postFetchFields);
		}

		return query.getSingleResult(em);
	}

	public AssetByMobileGuidLoader setMobileGuid(String mobileGuid) {
		this.mobileGuid = mobileGuid;
		return this;
	}

	public AssetByMobileGuidLoader addPostFetchFields(String...paths) {
		this.postFetchFields = Arrays.asList(paths);
		return this;
	}





}
