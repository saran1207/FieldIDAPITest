package com.n4systems.services;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Project;
import com.n4systems.tools.Pager;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class JobListService {

	private final PersistenceManager persistenceManager;
	private final SecurityFilter filter;
	private final int pageSize;
	private int pageNumber;
	private String orderBy;
	private boolean ascendingOrderBy;
	
	
	public JobListService(PersistenceManager persistenceManager, SecurityFilter filter, int pageSize) {
		super();
		this.persistenceManager = persistenceManager;
		this.filter = filter;
		this.pageSize = pageSize;
		this.pageNumber = 1;
		this.orderBy = "projectID";
		this.ascendingOrderBy = true;
	}
	
	
	public Pager<Project> getList(boolean justAssignedOn, boolean onlyOpen) {
		QueryBuilder<Project> qBuilder = new QueryBuilder<Project>(Project.class, filter.setDefaultTargets());
		qBuilder.setSimpleSelect().addOrder(orderBy, ascendingOrderBy).addSimpleWhere("retired", false);
		if (justAssignedOn) {
			qBuilder.addRequiredLeftJoin("resources", "resource");
			qBuilder.addWhere(new WhereParameter<Long>(Comparator.EQ, "resourceId", "resource.uniqueID", filter.getUserId(), null, true));
		}
		if (onlyOpen) {
			qBuilder.addSimpleWhere("open", true);
		}
		
		return persistenceManager.findAllPaged(qBuilder, pageNumber, pageSize);
	}
	
	
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = (pageNumber == null) ? 1 : pageNumber;
	}


	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	public void setAscendingOrderBy() {
		this.ascendingOrderBy = true;
	}
	public void setDescendingOrderBy() {
		this.ascendingOrderBy = false;
	}
}
