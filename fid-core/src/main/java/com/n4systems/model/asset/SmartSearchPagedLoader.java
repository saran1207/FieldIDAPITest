package com.n4systems.model.asset;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class SmartSearchPagedLoader extends Loader<Pager<Asset>> {

	private Integer page;
	private Integer pageSize = 10;
	private String searchText;
	private SecurityFilter filter;
	private Long assetTypeId;
	
	public SmartSearchPagedLoader(SecurityFilter filter) {
		this.filter = filter;
	}

	@Override
	public Pager<Asset> load(EntityManager em) {
		return createQuery(filter).getPaginatedResults(em, page, pageSize);
	}

	protected QueryBuilder<Asset> createQuery(SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
		
		//If we're smart-searching for assets from merge or during an event, for example, we'll
		//only want certain asset types to be retrieved. Otherwise we want everything. 
		if (assetTypeId!=null){
			builder.addSimpleWhere("type.id", assetTypeId);
		}
	
		builder.addWhere(new SmartSearchWhereClause(searchText, true, true, true));
		builder.addOrder("created");

		return builder;
	}

	public SmartSearchPagedLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}

	public SmartSearchPagedLoader setPage(Integer page) {
		this.page = page;
		return this;
	}

	public SmartSearchPagedLoader setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	public SmartSearchPagedLoader setAssetType(Long assetTypeId) {
		this.assetTypeId = assetTypeId;
		return this;
	}

}
