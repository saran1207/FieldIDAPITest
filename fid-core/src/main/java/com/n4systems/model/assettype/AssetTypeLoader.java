package com.n4systems.model.assettype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.TenantFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetTypeLoader extends TenantFilteredLoader<AssetType> {

	private Long id;

	private List<String> postFetchFields = new ArrayList<String>();

	public AssetTypeLoader(Long tenantId) {
		super(tenantId);
	}

	public AssetTypeLoader(SecurityFilter filter) {
		super(filter);
	}

	public AssetTypeLoader(Tenant tenant) {
		super(tenant);
	}

	public AssetTypeLoader(TenantOnlySecurityFilter filter) {
		super(filter);
	}

	@Override
	protected AssetType load(EntityManager em, TenantOnlySecurityFilter filter) {
		if (id == null) {
			throw new InvalidArgumentException("you must give an id");
		}
		
		QueryBuilder<AssetType> query = getQueryBuilder(filter);
		query.addSimpleWhere("id", id);
		query.getPostFetchPaths().addAll(postFetchFields);
		
		
		return query.getSingleResult(em);
	}

	private QueryBuilder<AssetType> getQueryBuilder(TenantOnlySecurityFilter filter) {
		return new QueryBuilder<AssetType>(AssetType.class, filter);
	}
	
	public AssetTypeLoader setId(Long id) {
		this.id = id;
		return this;
	}	
	
	public AssetTypeLoader setStandardPostFetches() {
		return setPostFetchFields("infoFields", "inspectionTypes", "attachments", "subTypes");
	}
	
	
	public AssetTypeLoader setPostFetchFields(String ...postFetchFields) {
		this.postFetchFields = Arrays.asList(postFetchFields);
		return this;
	}

}
