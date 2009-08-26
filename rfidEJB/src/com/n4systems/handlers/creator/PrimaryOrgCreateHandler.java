package com.n4systems.handlers.creator;

import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;

public interface PrimaryOrgCreateHandler extends ReversableCreateHandler<PrimaryOrg> {
	
	public PrimaryOrgCreateHandler forTenant(Tenant tenant);
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
	
}
