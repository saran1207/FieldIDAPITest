package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;
import com.n4systems.subscription.AccountCreationInformation;

public interface PrimaryOrgCreateHandler extends CreateHandler {

	public PrimaryOrgCreateHandler forTenant(Tenant tenant);
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
}
