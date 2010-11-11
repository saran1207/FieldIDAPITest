package com.n4systems.model.orgs;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.PaginatedLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class DivisionOrgPaginatedLoader extends PaginatedLoader<DivisionOrg> {
	private CustomerOrg customerOrg;
	private boolean withLinkedDivisions = true;
	private boolean archivedState;
	private boolean archivedOnly;
	private TenantOnlySecurityFilter archivedFilter;
	
	public DivisionOrgPaginatedLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected QueryBuilder<DivisionOrg> createBuilder(SecurityFilter filter) {
		
		archivedFilter = new TenantOnlySecurityFilter(filter);
		if(archivedState){
			archivedFilter.enableShowArchived();
		}

		QueryBuilder<DivisionOrg> builder = new QueryBuilder<DivisionOrg>(DivisionOrg.class, archivedFilter);
		builder.addOrder("name");
		
		if (customerOrg != null) {
			builder.addWhere(WhereClauseFactory.create("parent", customerOrg));
		}
		
		if (!withLinkedDivisions) {
			builder.addWhere(new WhereParameter<Object>(Comparator.NULL, "linkedOrg"));
		}
		
		if (archivedOnly) {
			builder.addWhere(Comparator.EQ, "state", "state", EntityState.ARCHIVED);
		}
		
		return builder;
	}
	
	public DivisionOrgPaginatedLoader setCustomerFilter(CustomerOrg customerFilter) {
		this.customerOrg = customerFilter;
		return this;
	}
	
	public DivisionOrgPaginatedLoader setWithLinkedDivisions(boolean withLinkedDivisions) {
		this.withLinkedDivisions = withLinkedDivisions;
		return this;
	}
	
	public DivisionOrgPaginatedLoader withArchivedState() {
		archivedState = true;
		return this;
	}
	
	public void setArchivedOnly(boolean archivedOnly) {
		withArchivedState();
		this.archivedOnly = archivedOnly;
	}
	
}
