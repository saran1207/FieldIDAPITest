package com.n4systems.persistence.loaders;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

abstract public class PaginatedLoader<T> extends SecurityFilteredLoader<Pager<T>> {
	private static final int FIRST_PAGE = 1;
	
	private int page = FIRST_PAGE;
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
	
	public PaginatedLoader<T> setFirstPage() {
		return setPage(FIRST_PAGE);
	}

}
