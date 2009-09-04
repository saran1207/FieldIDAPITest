package com.n4systems.handlers.creator.signup;

import rfid.ejb.entity.UserBean;

import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;

public class SubscriptionApprovalHandlerImpl implements SubscriptionApprovalHandler {

	final OrganizationSaver orgSaver;
	final UserSaver userSaver;
	
	SignUpTenantResponse subscriptionApproval;
	UserBean adminUser;
	PrimaryOrg primaryOrg;
	
	public SubscriptionApprovalHandlerImpl(OrganizationSaver orgSaver, UserSaver userSaver) {
		super();
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
	}
	
	
	public void applyApproval(Transaction transaction) {
		adminUser.setExternalId(subscriptionApproval.getClient().getExternalId());
		primaryOrg.setExternalId(subscriptionApproval.getTenant().getExternalId());
		userSaver.update(transaction, adminUser);
		orgSaver.update(transaction, primaryOrg);
	}
	
	public SubscriptionApprovalHandler forSubscriptionApproval(SignUpTenantResponse subscriptionApproval) {
		this.subscriptionApproval = subscriptionApproval;
		return this;
	}
	
	public SubscriptionApprovalHandler forAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder) {
		this.adminUser = accountPlaceHolder.getAdminUser();
		this.primaryOrg = accountPlaceHolder.getPrimaryOrg();
		return this;
	}
}
