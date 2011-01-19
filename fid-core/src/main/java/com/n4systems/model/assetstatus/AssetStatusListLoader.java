package com.n4systems.model.assetstatus;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AssetStatusListLoader extends ListLoader<AssetStatus> {

	private String[] postFetchFields = new String[0];
	private boolean archivedOnly = false;

	public AssetStatusListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetStatus> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetStatus> builder = new QueryBuilder<AssetStatus>(AssetStatus.class, new TenantOnlySecurityFilter(filter).setShowArchived(archivedOnly));
		builder.addOrder("name");
		if(archivedOnly) {
			builder.addSimpleWhere("state", EntityState.ARCHIVED);
		}else {
			builder.addSimpleWhere("state", EntityState.ACTIVE);
		}
		
		builder.addPostFetchPaths(postFetchFields);
		
		List<AssetStatus> assetStatuses = builder.getResultList(em);

		return assetStatuses;
	}

	public AssetStatusListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	public AssetStatusListLoader archivedOnly() {
		archivedOnly = true;
		return this;
	}

}
