package com.n4systems.handlers.creator.signup;

import com.n4systems.model.Tenant;

public interface SignupReferralHandler {
	public void processReferral(Tenant referralTenant, Tenant referredTenant, String referralCode);
}
