package com.n4systems.model.user;

import javax.persistence.EntityManager;


import com.n4systems.model.Tenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class UserByReferralCodeLoader extends Loader<User> {
	private Tenant tenant;
	private String referralCode;
	
	@Override
	public User load(EntityManager em) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new TenantOnlySecurityFilter(tenant));
		builder.addWhere(WhereClauseFactory.create("referralKey", referralCode));
		
		User user = builder.getSingleResult(em);
		return user;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public UserByReferralCodeLoader setTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public UserByReferralCodeLoader setReferralCode(String referralCode) {
		this.referralCode = referralCode;
		return this;
	}
}
