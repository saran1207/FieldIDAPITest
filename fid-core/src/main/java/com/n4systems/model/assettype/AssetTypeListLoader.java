package com.n4systems.model.assettype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class AssetTypeListLoader extends ListLoader<AssetType> {
	
	private String[] postFetchFields = new String[0];
	private String nameFilter;
	private Long groupFilter;

	public AssetTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AssetType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AssetType> builder = new QueryBuilder<AssetType>(AssetType.class, filter);
		
		if(nameFilter != null && !nameFilter.isEmpty()) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH);
		}
		
		if(groupFilter != null) {
			builder.addSimpleWhere("group.id", groupFilter);
		}
		
		builder.addOrder("name");
		builder.addPostFetchPaths(postFetchFields);
		return builder.getResultList(em);
	}
	
	public AssetTypeListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	public AssetTypeListLoader setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}

	public AssetTypeListLoader setGroupFilter(Long groupFilter) {
		this.groupFilter = groupFilter;
		return this;
	}

}
