package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;
import com.n4systems.subscription.SignUpTenantResponse;

public interface PrimaryOrgCreateHandler extends CreateHandler {

	public PrimaryOrgCreateHandler forTenant(Tenant tenant);
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
	public PrimaryOrgCreateHandler withApprovedSubscription(SignUpTenantResponse signUpTenantResponse);
}
