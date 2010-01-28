package com.n4systems.model.signup;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SignupReferralListLoader extends ListLoader<SignupReferral> {

	public SignupReferralListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<SignupReferral> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<SignupReferral> builder = new QueryBuilder<SignupReferral>(SignupReferral.class, filter);
		
		List<SignupReferral> referrals = builder.getResultList(em);
		return referrals;
	}

}
