package com.n4systems.persistence.loaders;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

abstract public class PaginatedLoader<T> extends SecurityFilteredLoader<Pager<T>> {
	private int page = 0;
	private int pageSize = 10;
	
	public PaginatedLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Pager<T> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<T> builder  = createBuilder(filter);
		Pager<T> pager = builder.getPaginatedResults(em, page, pageSize);
		return pager;
	}
	
	abstract protected QueryBuilder<T> createBuilder(SecurityFilter filter);

	public int getPage() {
		return page;
	}

	public PaginatedLoader<T> setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public PaginatedLoader<T> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

}
