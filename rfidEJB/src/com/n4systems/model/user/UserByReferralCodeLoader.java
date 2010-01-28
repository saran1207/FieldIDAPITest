package com.n4systems.model.user;

import javax.persistence.EntityManager;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.Tenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class UserByReferralCodeLoader extends Loader<UserBean> {
	private Tenant tenant;
	private String referralCode;
	
	@Override
	protected UserBean load(EntityManager em) {
		QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, new TenantOnlySecurityFilter(tenant));
		builder.addWhere(WhereClauseFactory.create("referralKey", referralCode));
		
		UserBean user = builder.getSingleResult(em);
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
