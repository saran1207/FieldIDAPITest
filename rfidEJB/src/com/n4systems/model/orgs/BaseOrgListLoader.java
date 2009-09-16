package com.n4systems.model.orgs;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class BaseOrgListLoader extends ListLoader<BaseOrg> {
	public enum OrgType {
		ALL_ORGS("all"),
		NON_PRIMARY("non_primary"), 
		INTERNAL("internal"),
		EXTERNAL("external"),
		CUSTOMER("customer"),
		DIVISION("division"),
		PRIMARY("primary"),
		SECONDARY("secondary");
		
		private String inputName;
		
		private OrgType(String inputName) {
			this.inputName = inputName;
		}
		
		public String getInputName() {
			return inputName;
		}
		
		public static OrgType find(String inputName) {
			for (OrgType type : OrgType.values()) {
				if (type.inputName.equals(inputName)) {
					return type;
				}
			}
			throw new RuntimeException("could not find input type");
		}
	}
	
	private String searchName;
	private OrgType orgType = OrgType.ALL_ORGS;
	
	public BaseOrgListLoader(SecurityFilter filter) {
		super(filter);
	}

	
	@Override
	protected List<BaseOrg> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<BaseOrg> baseOrgQuery = getQueryBuilder(filter);
		if (searchName != null) {
			baseOrgQuery.addWhere(Comparator.LIKE, "searchName", "name", searchName, WhereParameter.WILDCARD_BOTH);
		}
		
		applyOrgTypeFiltering(filter, baseOrgQuery);
		
		baseOrgQuery.addOrder("name");
		return baseOrgQuery.getResultList(em, 0, 10);
	}


	private void applyOrgTypeFiltering(SecurityFilter filter, QueryBuilder<BaseOrg> baseOrgQuery) {
		switch (orgType) {
			case NON_PRIMARY:
				new NonPrimaryOrgFilter(filter.getOwner().getPrimaryOrg()).applyFilter(baseOrgQuery);
				break;
			case EXTERNAL:
				new ExternalOrgFilter().applyFilter(baseOrgQuery);
				break;
			case INTERNAL:
				new InternalOrgFilter().applyFilter(baseOrgQuery);
				break;
			case ALL_ORGS:
			default:
				break;
		}
	}


	protected QueryBuilder<BaseOrg> getQueryBuilder(SecurityFilter filter) {
		QueryBuilder<BaseOrg> baseOrgQuery = new QueryBuilder<BaseOrg>(BaseOrg.class, filter);
		return baseOrgQuery;
	}

	public BaseOrgListLoader setSearchName(String searchName) {
		this.searchName = searchName;
		return this;
	}
	
	public BaseOrgListLoader setOrgType(String orgType) {
		this.orgType = OrgType.find(orgType);
		return this;
	}

}
