package com.n4systems.handlers.creator;

import com.n4systems.model.signup.AccountCreationInformation;

public interface TenantCreateHandler extends CreateHandler {

	public TenantCreateHandler forAccountInfo(AccountCreationInformation accountInfo);
}
