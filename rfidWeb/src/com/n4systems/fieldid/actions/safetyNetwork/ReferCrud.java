package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.signup.SignupReferral;

public class ReferCrud extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private List<SignupReferral> referrals;
	
	public ReferCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	public String doShow() {
		referrals = getLoaderFactory().createSignupReferralListLoader().load();
		
		
		return SUCCESS;
	}
	
}
