package com.n4systems.model.savedreports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;


import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class SharedReportUserListLoader extends ListLoader<User> {

	private SavedReport report;
		
	public SharedReportUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<User> load(EntityManager em, SecurityFilter filter) {
		Set<User> users = new HashSet<User>();
		
		users.addAll(getAllUsersBelowTheOwnerOnTheReport(em, filter));
		
		users.addAll(getAllUsersDirectlyAboveTheOwnerOnTheReport(em, filter));
		
		users.addAll(getAllSecondaryOrgUsersIfReportOwnerIsNotUnderASecondaryOrg(em, filter));
		
		users.removeAll(userGoingToShareTheReport(em, filter));
		return new ArrayList<User>(users);
	}


	private List<User> getAllSecondaryOrgUsersIfReportOwnerIsNotUnderASecondaryOrg(EntityManager em, SecurityFilter filter) {
		List<User> users = new ArrayList<User>();
		if (getOwnerForReport(em, filter).getSecondaryOrg() == null) {
			QueryBuilder<User> query = new QueryBuilder<User>(User.class, filter);
			
			query.addWhere(Comparator.NOTNULL, "owner.secondaryOrg", "owner.secondaryOrg", -1);
			query.addWhere(Comparator.NULL, "owner.customerOrg", "owner.customerOrg", -1);
			users.addAll(query.getResultList(em));
		}
		
		return users;
	}

	private List<User> getAllUsersDirectlyAboveTheOwnerOnTheReport(EntityManager em, SecurityFilter filter) {
		List<User> users = new ArrayList<User>();
		BaseOrg org = getOwnerForReport(em, filter);
		
		while (org.getParent() != null) {
			org = org.getParent();
			loadUsersForOrg(em, filter, users, org);
		}
		
		return users;
	}

	private void loadUsersForOrg(EntityManager em, SecurityFilter filter, List<User> users, BaseOrg org) {
		QueryBuilder<User> query = new QueryBuilder<User>(User.class, filter);
		query.addSimpleWhere("owner", org);
		users.addAll(query.getResultList(em));
	}


	private List<User> getAllUsersBelowTheOwnerOnTheReport(EntityManager em, SecurityFilter filter) {
		QueryBuilder<User> usersBelowOwnerQuery = new QueryBuilder<User>(User.class, filter);
	
		getOwnerFilterForReport(em, filter).applyFilter(usersBelowOwnerQuery);
		
		return usersBelowOwnerQuery.getResultList(em);
	}		

	private OwnerAndDownFilter getOwnerFilterForReport(EntityManager em, SecurityFilter filter) {
		return new OwnerAndDownFilter(getOwnerForReport(em, filter));
	}

	private BaseOrg getOwnerForReport(EntityManager em, SecurityFilter filter) {
		return em.find(BaseOrg.class, getReportOwnerId(filter));
	}

	private Long getReportOwnerId(SecurityFilter filter) {
		Long reportsOwnerId = report.getLongCriteria(SavedReport.OWNER_ID);
		
		if (reportsOwnerId == null) {
			reportsOwnerId = filter.getOwner().getId();
		}
		
		return reportsOwnerId;
	}
	
	private Set<User> userGoingToShareTheReport(EntityManager em, SecurityFilter filter) {
		Set<User> user = new HashSet<User>(1);
		if (filter.getUserId() != null) {
			user.add(em.find(User.class, filter.getUserId()));
		}
		return user;
	}

	public SharedReportUserListLoader setReport(SavedReport report) {
		this.report = report;
		return this;
	}
	
}
