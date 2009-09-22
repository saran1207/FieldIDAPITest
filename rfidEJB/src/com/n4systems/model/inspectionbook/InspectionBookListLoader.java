package com.n4systems.model.inspectionbook;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.InspectionBook;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

/**
 * Loads a list of inspection books.  Note that the security on this list is backwards to our normal security model.
 * Each owner level is allowed to see itself and all the way up the parent chain but not down the chain.  Eg, 
 * CustomerOrgs will see their Customer, the parent SecondaryOrg and the PrimaryOrg.
 */
public class InspectionBookListLoader extends ListLoader<InspectionBook> {

	private BaseOrg owner;
	private boolean openBooksOnly;
	
	public InspectionBookListLoader(SecurityFilter filter) {
		super(filter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<InspectionBook> load(EntityManager em, SecurityFilter filter) {
		/*
		 * This query should be moved to the QueryBuilder when it is capable of understanding 
		 * grouping.
		 */
		StringBuilder inspectionBookQuery = new StringBuilder("SELECT i FROM ");
		inspectionBookQuery.append(InspectionBook.class.getName());
		inspectionBookQuery.append(" i WHERE ");
		inspectionBookQuery.append("i.tenant.id = :security_tenant_id");
		inspectionBookQuery.append(" AND ");
		
		if (openBooksOnly) {
			inspectionBookQuery.append("i.open = true");
			inspectionBookQuery.append(" AND ");
		}
		
		applyOrgFilter(inspectionBookQuery, filter.getOwner(), "security");
		applyOrgFilter(inspectionBookQuery, owner, "owner");

		Query query = em.createQuery(inspectionBookQuery.toString());
		
		query.setParameter("security_tenant_id", owner.getTenant().getId());
		applyOrgParams(query, filter.getOwner(), "security");
		applyOrgParams(query, owner, "owner");
		
		return query.getResultList();
	}

	private void applyOrgFilter(StringBuilder query, BaseOrg owner, String paramPrefix) {
		query.append("(");
		if (owner.getDivisionOrg() != null) {
			query.append("(i.owner.divisionOrg.id = :").append(paramPrefix).append("_division_id)");
			query.append(" OR ");
		}
		
		if (owner.getCustomerOrg() != null) {
			query.append("(i.owner.customerOrg.id = :").append(paramPrefix).append("_customer_id AND i.owner.divisionOrg IS NULL)");
			query.append(" OR ");
		}
		
		if (owner.getSecondaryOrg() != null) {
			query.append("(i.owner.secondaryOrg.id = :").append(paramPrefix).append("_secondary_id AND i.owner.customerOrg IS NULL AND i.owner.divisionOrg IS NULL)");
			query.append(" OR ");
		}
		
		query.append("(owner.secondaryOrg IS NULL AND owner.customerOrg IS NULL AND owner.divisionOrg IS NULL)");
		query.append(")");
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
