package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.ReversableCreateHandler;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public interface PrimaryOrgCreateHandler extends ReversableCreateHandler<PrimaryOrg> {
	
	public PrimaryOrgCreateHandler forTenant(Tenant tenant);
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
	
}
