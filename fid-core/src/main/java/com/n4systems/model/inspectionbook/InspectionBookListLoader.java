package com.n4systems.model.inspectionbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * Loads a list of inspection books.  Note that the security on this list is backwards to our normal security model.
 * Each owner level is allowed to see itself and all the way up the parent chain but not down the chain.  Eg, 
 * CustomerOrgs will see their Customer, the parent SecondaryOrg and the PrimaryOrg.
 */
public class InspectionBookListLoader extends ListLoader<EventBook> {

	private BaseOrg owner;
	private boolean openBooksOnly;
	
	public InspectionBookListLoader(SecurityFilter filter) {
		super(filter);
	}

	public List<ListingPair> loadListingPair() {
		List<EventBook> eventBooks = load();
		return ListHelper.longListableToListingPair(eventBooks);
	}
	
	@Override
	protected List<EventBook> load(EntityManager em, SecurityFilter filter) {
		/*
		 * This query should be moved to the QueryBuilder when it is capable of understanding 
		 * grouping.
		 */
		List<EventBook> downwardTreeList = new ArrayList<EventBook>();
		List<EventBook> upwardTreeList = new ArrayList<EventBook>();
		
		if (owner == null) {
			downwardTreeList = getDownwardTreeList(em, filter);
			upwardTreeList = getUpwardTreeList(filter.getOwner(), em, filter);
		} else {
			upwardTreeList = getUpwardTreeList(owner, em, filter);
		}
		
		Set<EventBook> combinedSet = new TreeSet<EventBook>();
		combinedSet.addAll(downwardTreeList);
		combinedSet.addAll(upwardTreeList);
		
		return new ArrayList<EventBook>(combinedSet);
	}
	
	private StringBuilder getStartOfQuery() {
		StringBuilder queryString = new StringBuilder("SELECT i FROM ");
		queryString.append(EventBook.class.getName());
		queryString.append(" i WHERE ");
		queryString.append("i.tenant.id = :security_tenant_id");
		
		if (openBooksOnly) {
			queryString.append(" AND ");
			queryString.append("i.open = true");
		}
		
		return queryString;
	}
	
	private void applyTenantSecurity(SecurityFilter filter, Query query) {
		query.setParameter("security_tenant_id", filter.getTenantId());								
	}
	
	@SuppressWarnings("unchecked")
	private List<EventBook> getDownwardTreeList(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventBook> queryBuilder = new QueryBuilder<EventBook>(EventBook.class, filter);
		if (openBooksOnly) {
			queryBuilder.addSimpleWhere("open", true);
		}
		
		Query query = queryBuilder.createQuery(em);
		return query.getResultList();		
	}

	@SuppressWarnings("unchecked")
	private List<EventBook> getUpwardTreeList(BaseOrg owner, EntityManager em, SecurityFilter filter) {
		StringBuilder inspectionBookQuery = getStartOfQuery();
		inspectionBookQuery.append(" AND ");
		
		inspectionBookQuery.append("(");
		if (owner.getDivisionOrg() != null) {
			inspectionBookQuery.append("(i.owner.divisionOrg.id = :owner_division_id)");
			inspectionBookQuery.append(" OR ");
		}
		
		if (owner.getCustomerOrg() != null) {
			inspectionBookQuery.append("(i.owner.customerOrg.id = :owner_customer_id AND i.owner.divisionOrg IS NULL)");
			inspectionBookQuery.append(" OR ");
		}
		
		if (owner.getSecondaryOrg() != null) {
			inspectionBookQuery.append("(i.owner.secondaryOrg.id = :owner_secondary_id AND i.owner.customerOrg IS NULL AND i.owner.divisionOrg IS NULL)");
			inspectionBookQuery.append(" OR ");
		}
		
		inspectionBookQuery.append("(owner.secondaryOrg IS NULL AND owner.customerOrg IS NULL AND owner.divisionOrg IS NULL)");
		inspectionBookQuery.append(")");

		Query query = em.createQuery(inspectionBookQuery.toString());
		
		applyOrgParams(query, owner, "owner");
		applyTenantSecurity(filter, query);
		
		return query.getResultList();
	}
	
	private void applyOrgParams(Query query, BaseOrg owner, String paramPrefix) {
		if (owner.getDivisionOrg() != null) {
			query.setParameter(paramPrefix + "_division_id", owner.getDivisionOrg().getId());
		}
		
		if (owner.getCustomerOrg() != null) {
			query.setParameter(paramPrefix + "_customer_id", owner.getCustomerOrg().getId());
		}
		
		if (owner.getSecondaryOrg() != null) {
			query.setParameter(paramPrefix + "_secondary_id", owner.getSecondaryOrg().getId());
		}		
	}

	public InspectionBookListLoader setOwner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}

	public InspectionBookListLoader setOpenBooksOnly(boolean openBooksOnly) {
		this.openBooksOnly = openBooksOnly;
		return this;
	}
	
}
