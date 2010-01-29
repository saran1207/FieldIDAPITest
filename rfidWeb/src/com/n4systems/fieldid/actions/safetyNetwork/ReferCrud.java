package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.signup.SignupReferral;
import com.n4systems.services.TenantCache;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

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
	
	public List<SignupReferral> getReferrals() {
		return referrals;
	}
	
	public String getReferralUrl() {
		String signupPath = ConfigContext.getCurrentContext().getString(ConfigEntry.SIGNUP_PATH);
		return new SignupUrlBuilder(getBaseURI(), getUser(), signupPath).build();
	}
	
	public String getCompanyName(Long id) {
		return TenantCache.getInstance().findPrimaryOrg(id).getName();
	}
}
