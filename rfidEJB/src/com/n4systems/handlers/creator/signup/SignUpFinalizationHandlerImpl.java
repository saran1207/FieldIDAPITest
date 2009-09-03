package com.n4systems.handlers.creator.signup;

import java.util.Set;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.SignUpTenantResponse;
import com.n4systems.util.DataUnit;

public class SignUpFinalizationHandlerImpl implements SignUpFinalizationHandler {

	private final ExtendedFeatureListResolver extendedFeatureListResolver;
	private final OrganizationSaver orgSaver;
	private final UserSaver userSaver;
	
	private AccountCreationInformation accountInformation;
	private AccountPlaceHolder accountPlaceHolder;
	private SignUpTenantResponse subscriptionApproval;
	

	public SignUpFinalizationHandlerImpl(ExtendedFeatureListResolver extendedFeatureListResolver, OrganizationSaver orgSaver, UserSaver userSaver) {
		super();
		this.extendedFeatureListResolver = extendedFeatureListResolver;
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
	}

	
	public void finalizeSignUp(Transaction transaction) {
		guards();
		
		processExtendedFeatures(transaction);
		processLimits();
		applyExternalIds();
		
		saveChanges(transaction);
	}


	private void processLimits() {
		TenantLimit limits = new TenantLimit();
		
		limits.setDiskSpaceInBytes(DataUnit.MEGABYTES.convertTo(accountInformation.getSignUpPackage().getDiskSpaceInMB(), DataUnit.BYTES));
		limits.setAssets(accountInformation.getSignUpPackage().getAssets());
		limits.setUsers(accountInformation.getNumberOfUsers().longValue());
		
		getPrimaryOrg().setLimits(limits);
		
	}


	private void saveChanges(Transaction transaction) {
		orgSaver.saveOrUpdate(transaction, getPrimaryOrg());
		userSaver.saveOrUpdate(transaction, getAdminUser());
	}


	private void applyExternalIds() {
		getAdminUser().setExternalId(subscriptionApproval.getClient().getExternalId());
		getPrimaryOrg().setExternalId(subscriptionApproval.getTenant().getExternalId());
	}


	private UserBean getAdminUser() {
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
	}

	private void processExtendedFeatures(Transaction transaction) {
		Set<ExtendedFeature> extendedFeaturesToTurnOn = extendedFeatureListResolver.withPromoCode(accountInformation.getPromoCode())
												.withSignUpPackageDetails(accountInformation.getSignUpPackage().getSignPackageDetails())
												.resolve(transaction);
		
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
	
	
	protected ExtendedFeatureSwitch getSwitchFor(ExtendedFeature feature) {
		return ExtendedFeatureFactory.getSwitchFor(feature, getPrimaryOrg());
	}


	
}
