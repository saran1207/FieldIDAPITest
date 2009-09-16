package com.n4systems.model.savedreports;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.OwnerFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SharedReportUserListLoader extends ListLoader<UserBean> {

	private SavedReport report;
		
	public SharedReportUserListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<UserBean> load(EntityManager em, SecurityFilter filter) {
		List<UserBean> users = new ArrayList<UserBean>();
		users.addAll(getAllUsersBelowTheOwnerOnTheReport(em, filter));
		users.addAll(getAllUsersDirectlyAboveTheOwnerOnTheReport(em, filter));
		return users;
	}


	private List<UserBean> getAllUsersDirectlyAboveTheOwnerOnTheReport(EntityManager em, SecurityFilter filter) {
		List<UserBean> users = new ArrayList<UserBean>();
		BaseOrg org = getOwnerForReport(em, filter);
		
		while (org.getParent() != null) {
			org = org.getParent();
			loadUsersForOrg(em, filter, users, org);
		}
		
		return users;
	}

	private void loadUsersForOrg(EntityManager em, SecurityFilter filter, List<UserBean> users, BaseOrg org) {
		QueryBuilder<UserBean> query = new QueryBuilder<UserBean>(UserBean.class, filter);
		query.addSimpleWhere("owner", org);
		users.addAll(query.getResultList(em));
	}


	private List<UserBean> getAllUsersBelowTheOwnerOnTheReport(EntityManager em, SecurityFilter filter) {
		QueryBuilder<UserBean> usersBelowOwnerQuery = new QueryBuilder<UserBean>(UserBean.class, filter);
	
		getOwnerFilterForReport(em, filter).applyFilter(usersBelowOwnerQuery);
		
		return usersBelowOwnerQuery.getResultList(em);
	}		

	private OwnerFilter getOwnerFilterForReport(EntityManager em, SecurityFilter filter) {
		return new OwnerFilter(getOwnerForReport(em, filter));
	}

	private BaseOrg getOwnerForReport(EntityManager em, SecurityFilter filter) {
		return em.find(BaseOrg.class, getReportOwnerId(filter));
	}

	private Long getReportOwnerId(SecurityFilter filter) {
		Long reportsOwnerId = report.getLongCriteria(SavedReport.OWNER_ID);
		
		if (reportsOwnerId == null) {
			reportsOwnerId = filter.getOwner().getPrimaryOrg().getId();
		}
		
		return reportsOwnerId;
	}

	public SharedReportUserListLoader setReport(SavedReport report) {
		this.report = report;
		return this;
	}
	
}
