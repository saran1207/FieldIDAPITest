package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

public class AllPrimaryOrgsPaginatedLoader extends Loader<Pager<PrimaryOrg>> {
	
	private static final int FIRST_PAGE = 1;	
	private int page = FIRST_PAGE;
	private int pageSize = 10;
	
	private String order;
	private boolean ascending;

	@Override
	protected Pager<PrimaryOrg> load(EntityManager em) {
		QueryBuilder<PrimaryOrg> builder  =  new QueryBuilder<PrimaryOrg>(PrimaryOrg.class, new OpenSecurityFilter());
		
		if(order != null && !order.isEmpty()) {
			builder.addOrder(order, ascending);
		}
		
		Pager<PrimaryOrg> pager = builder.getPaginatedResults(em, page, pageSize);
		return pager;
	}
	

	public int getPage() {
		return page;
	}

	public AllPrimaryOrgsPaginatedLoader setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public AllPrimaryOrgsPaginatedLoader setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}
	
	public AllPrimaryOrgsPaginatedLoader setFirstPage() {
		return setPage(FIRST_PAGE);
	}


	public AllPrimaryOrgsPaginatedLoader setOrder(String order, boolean ascending) {
		this.order = order;
		this.ascending = ascending;		
		return this;
	}


}
