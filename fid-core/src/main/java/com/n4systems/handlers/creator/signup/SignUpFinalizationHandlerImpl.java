package com.n4systems.handlers.creator.signup;

import java.util.List;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;

public class SignUpFinalizationHandlerImpl implements SignUpFinalizationHandler {
	private final OrgSaver orgSaver;
	private final UserSaver userSaver;
	private final LinkTenantHandler linkTenantHandler;

	private AccountCreationInformation accountInformation;
	private AccountPlaceHolder accountPlaceHolder;
	private SignUpTenantResponse subscriptionApproval;
	private PrimaryOrg referrerOrg;

	public SignUpFinalizationHandlerImpl(OrgSaver orgSaver, UserSaver userSaver, LinkTenantHandler linkTenantHandler) {
		super();
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
		this.linkTenantHandler = linkTenantHandler;

	}

	public void finalizeSignUp(Transaction transaction) {
		guards();

		linkTenants(transaction);
		applyChangesToPlaceHolders(transaction);
	}

	private void applyChangesToPlaceHolders(Transaction transaction) {
		processExtendedFeatures(transaction);
		processLimits(transaction);
		applyExternalIds();
		saveChanges(transaction);
	}

	private void processLimits(Transaction transaction) {
		accountPlaceHolder.getTenant().getSettings().setMaxEmployeeUsers(accountInformation.getNumberOfUsers());
	}

	private void linkTenants(Transaction transaction) {
		linkTenantHandler.setAccountPlaceHolder(accountPlaceHolder).setReferrerOrg(referrerOrg);

		linkTenantHandler.link(transaction);
	}

	private void saveChanges(Transaction transaction) {
		orgSaver.saveOrUpdate(transaction, getPrimaryOrg());
		userSaver.saveOrUpdate(transaction, getAdminUser());
	}

	private void applyExternalIds() {
		getAdminUser().setExternalId(subscriptionApproval.getClient().getExternalId());
		getPrimaryOrg().setExternalId(subscriptionApproval.getTenant().getExternalId());
	}

	private User getAdminUser() {
		return accountPlaceHolder.getAdminUser();
	}

	private PrimaryOrg getPrimaryOrg() {
		return accountPlaceHolder.getPrimaryOrg();
	}

	private void guards() {
		if (accountInformation == null) {
			throw new InvalidArgumentException("you must give an Account Creation Information");
		}

		if (accountPlaceHolder == null) {
			throw new InvalidArgumentException("you must give an Account Place Holder");
		}

		if (subscriptionApproval == null) {
			throw new InvalidArgumentException("you must give a SignUpTenantResponse");
		}

		if (referrerOrg == null) {
			throw new InvalidArgumentException("you must give a referrer PrimaryOrg");
		}
	}

	private void processExtendedFeatures(Transaction transaction) {
		List<ExtendedFeature> extendedFeaturesToTurnOn = accountInformation.getSignUpPackage().getSignPackageDetails().getFeatureList();
		for (ExtendedFeature feature : extendedFeaturesToTurnOn) {
			ExtendedFeatureSwitch featureSwitch = getSwitchFor(feature);
			featureSwitch.enableFeature(transaction);
		}
	}

	public SignUpFinalizationHandler setAccountInformation(AccountCreationInformation accountInformation) {
		this.accountInformation = accountInformation;
		return this;
	}

	public SignUpFinalizationHandler setAccountPlaceHolder(AccountPlaceHolder accountPlaceHolder) {
		this.accountPlaceHolder = accountPlaceHolder;
		return this;
	}

	public SignUpFinalizationHandler setSubscriptionApproval(SignUpTenantResponse subscriptionApproval) {
		this.subscriptionApproval = subscriptionApproval;
		return this;
	}

	public SignUpFinalizationHandler setReferrerOrg(PrimaryOrg referrerOrg) {
		this.referrerOrg = referrerOrg;
		return this;
	}

	protected ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature) {
		return ExtendedFeatureFactory.getSwitchFor(feature, getPrimaryOrg());
	}

}
