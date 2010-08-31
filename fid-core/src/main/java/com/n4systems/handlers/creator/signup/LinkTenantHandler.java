package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;

public interface LinkTenantHandler {

	public void link(Transaction transaction);

	public LinkTenantHandler setAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder);

	public LinkTenantHandler setReferrerOrg(PrimaryOrg referrerOrg);

}