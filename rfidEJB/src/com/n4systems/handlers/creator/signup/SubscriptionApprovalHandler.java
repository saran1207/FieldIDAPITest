package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;

public interface SubscriptionApprovalHandler {

	public void applyApproval(Transaction transaction);

	public SubscriptionApprovalHandler forSubscriptionApproval(SignUpTenantResponse subscriptionApproval);

	public SubscriptionApprovalHandler forAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder);

}
