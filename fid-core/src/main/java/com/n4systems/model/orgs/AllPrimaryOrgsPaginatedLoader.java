package com.n4systems.model.orgs;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.utils.DateRange;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.tools.Pager;
import com.n4systems.util.chart.RangeType;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.joda.time.LocalDate;

import javax.persistence.EntityManager;

public class AllPrimaryOrgsPaginatedLoader extends Loader<Pager<PrimaryOrg>> {
	
	private static final int FIRST_PAGE = 1;	
	private int page = FIRST_PAGE;
	private int pageSize = 10;
	
	private String nameFilter;
	private String order;
	private boolean ascending;
    private RangeType inactiveSince;
    private boolean activeOnly;

	@Override
	public Pager<PrimaryOrg> load(EntityManager em) {
		QueryBuilder<PrimaryOrg> builder  =  new QueryBuilder<PrimaryOrg>(PrimaryOrg.class, new OpenSecurityFilter());

		if(nameFilter != null && !nameFilter.isEmpty()) {
            WhereParameterGroup nameGroup = new WhereParameterGroup("nameGroup");

            nameGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "tenant.name", nameFilter, WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            nameGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "name", nameFilter, WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));

            builder.addWhere(nameGroup);
		}
		
		if(order != null && !order.isEmpty()) {
			builder.addOrder(order, ascending);
		}

        if (inactiveSince != null) {
            final LocalDate from = new DateRange(inactiveSince).getFrom();
            builder.addWhere(WhereClauseFactory.create(Comparator.LE, "tenant.lastLoginTime", from==null ? null : from.toDate()));
        }

        if (activeOnly) {
            builder.addWhere(WhereClauseFactory.create(Comparator.EQ, "tenant.disabled", false));
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


	public AllPrimaryOrgsPaginatedLoader setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}

    public AllPrimaryOrgsPaginatedLoader setInactiveSince(RangeType inactiveSince) {
        this.inactiveSince = inactiveSince;
        return this;
    }

    public AllPrimaryOrgsPaginatedLoader setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
        return this;
    }
}
