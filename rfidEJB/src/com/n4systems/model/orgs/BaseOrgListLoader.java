package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class BaseOrgListLoader extends ListLoader<BaseOrg> {

	private String searchName;
	
	public BaseOrgListLoader(SecurityFilter filter) {
		super(filter);
	}

	
	@Override
	protected List<BaseOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<BaseOrg> baseOrgQuery = getQueryBuilder(filter);
		if (searchName != null) {
			baseOrgQuery.addWhere(Comparator.LIKE, "searchName", "name", searchName, WhereParameter.WILDCARD_BOTH);
		}
		baseOrgQuery.addOrder("name");
		return baseOrgQuery.getResultList(em, 0, 10);
	}


	protected QueryBuilder<BaseOrg> getQueryBuilder(SecurityFilter filter) {
		QueryBuilder<BaseOrg> baseOrgQuery = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		return baseOrgQuery;
	}

	public BaseOrgListLoader setSearchName(String searchName) {
		this.searchName = searchName;
		return this;
	}

}
