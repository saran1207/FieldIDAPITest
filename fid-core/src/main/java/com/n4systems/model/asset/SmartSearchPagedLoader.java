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

	public SmartSearchPagedLoader(SecurityFilter filter) {
		this.filter = filter;
	}

	@Override
	public Pager<Asset> load(EntityManager em) {
		return createQuery(filter).getPaginatedResults(em, page, pageSize);
	}

	protected QueryBuilder<Asset> createQuery(SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
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

}
