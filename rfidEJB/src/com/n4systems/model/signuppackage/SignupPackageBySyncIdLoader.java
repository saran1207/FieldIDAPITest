package com.n4systems.model.signuppackage;

import javax.persistence.EntityManager;

import com.n4systems.model.SignupPackage;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class SignupPackageBySyncIdLoader extends Loader<SignupPackage> {

	private String syncId;
	
	@Override
	protected SignupPackage load(EntityManager em) {
		QueryBuilder<SignupPackage> builder = new QueryBuilder<SignupPackage>(SignupPackage.class);
		builder.addSimpleWhere("syncId", syncId);
		
		SignupPackage signupPackage = builder.getSingleResult(em);
		
		return signupPackage;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

}
